package com.bendertales.mc.chatapi.impl;

import java.util.*;
import java.util.regex.Pattern;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.impl.messages.FormattedMessage;
import com.bendertales.mc.chatapi.impl.vo.Channel;
import com.bendertales.mc.chatapi.impl.vo.MessageOptions;
import com.bendertales.mc.chatapi.impl.vo.ModSettings;
import com.bendertales.mc.chatapi.impl.vo.PlayerChannelStatus;
import com.bendertales.mc.talesservercommon.helpers.Perms;
import fr.bendertales.mc.channels.api.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class ChatManager implements Messenger {

	private static final Pattern FORMATTING_REGEX = Pattern.compile("§[a-zA-Z0-9]");
	private static final ChatManager instance = new ChatManager();

	public static ChatManager get() {
		// Not a big fan of using singleton but necessary for the mixin
		return instance;
	}

	private ChatManager() {
	}

	private final ModSettingsRepository modSettingsRepository = new ModSettingsRepository();
	private final PlayerDataRepository  playerManager         = new PlayerDataRepository();

	private ModSettings              modSettings;
	private Map<Identifier, Channel> channelsById;
	private MinecraftServer minecraftServer;

	public void reload() {
		playerManager.clearCache();
		load();
	}

	public void load() {
		this.modSettings = modSettingsRepository.getConfig();
		this.channelsById = modSettings.channels();

		var channel = channelsById.get(modSettings.defaultChannel());
		if (channel == null) {
			channel = channelsById.get(ChatConstants.Ids.Channels.GLOBAL);
			if (channel == null) {
				channel = channelsById.values().stream().findFirst().orElse(null);
			}
		}

		this.playerManager.setDefaultChannel(channel == null ? null : channel.id());
	}

	public void setMinecraftServer(MinecraftServer minecraftServer) {
		this.minecraftServer = minecraftServer;
	}

	public void handleMessage(ServerPlayerEntity sender, String message) throws ChatException {
		var channel = extractChannelFromMessage(message);
		if (channel == null) {
			channel = getPlayerCurrentChannel(sender);
		}
		else {
			message = message.substring(channel.selectorPrefix().length());
		}

		sendMessage(sender, message, channel);
	}

	public void sendMessage(ServerPlayerEntity sender, String message, Identifier channelId) throws ChatException {
		var channel = channelsById.get(channelId);
		if (channel == null) {
			throw new ChatException("Channel not found");
		}

		sendMessage(sender, message, channel);
	}

	private void sendMessage(ServerPlayerEntity sender, String messageContent, Channel channel) throws ChatException {
		ensureSenderIsAllowedInChannel(sender, channel);

		var message = new Message(sender, messageContent);
		var formattedMessage = channel.messageFormatter().prepare(message);
		logToServer(sender, formattedMessage);
		getPlayers().stream()
            .filter(p -> isChannelVisibleForPlayer(channel, p))
			.forEach(recipient -> {
				var rOptions = new RecipientFilterOptions(hasEnabledSocialSpy(recipient));
				var visibility = channel.recipientsFilter().filterRecipient(sender, recipient, rOptions);
				if (visibility.isVisible()) {
					var mOptions = new MessageOptions(visibility == MessageVisibility.SOCIAL_SPY);
					recipient.sendMessage(formattedMessage.forRecipient(recipient, mOptions), false);
				}
			});
	}

	private void logToServer(ServerPlayerEntity sender, FormattedMessage formattedMessage) {
		var text = formattedMessage.forRecipient(null, new MessageOptions(false));
		var consoleAdaptedMessage = FORMATTING_REGEX.matcher(text.getString()).replaceAll("");
		minecraftServer.sendMessage(Text.of(consoleAdaptedMessage));
	}

	public void respondToPrivateMessage(ServerPlayerEntity sender, String messageContent) throws ChatException {
		var senderSettings = playerManager.get(sender);
		var lastSenderUuid = senderSettings.getLastMessageSender();
		if (lastSenderUuid == null) {
			throw new ChatException("Nobody sent you a message recently");
		}

		var lastSender = minecraftServer.getPlayerManager().getPlayer(lastSenderUuid);

		sendPrivateMessage(sender, lastSender, messageContent);
	}

	public void sendPrivateMessage(ServerPlayerEntity sender, ServerPlayerEntity recipient, String messageContent)
	throws ChatException {
		if (recipient == null || recipient.isDisconnected()) {
			throw new ChatException("This player is not connected");
		}

		var options = new MessageOptions(false);
		var message = new Message(sender, messageContent);
		var pmFormats = modSettings.privateMessageFormatters();
		var formattedMessage = pmFormats.consoleFormatter().prepare(message);
		minecraftServer.sendMessage(formattedMessage.forRecipient(recipient, options));

		formattedMessage = pmFormats.senderIsYouFormatter().prepare(message);
		sender.sendMessage(formattedMessage.forRecipient(recipient, options), false);

		formattedMessage = pmFormats.senderIsOtherFormatter().prepare(message);
		recipient.sendMessage(formattedMessage.forRecipient(recipient, options), false);

		playerManager.update(recipient, settings -> {
			settings.setLastMessageSender(sender.getUuid());
		});
	}

	private void ensureSenderIsAllowedInChannel(ServerPlayerEntity sender, Channel channel) throws ChatException {
		if (!channel.senderFilter().test(sender)) {
			throw new ChatException("You cannot send a message in this channel.");
		}

		if (playerManager.get(sender).isMutedInChannel(channel)) {
			throw new ChatException("You are muted in this channel.");
		}
	}

	private boolean hasEnabledSocialSpy(ServerPlayerEntity player) {
		if (playerManager.get(player).isEnabledSocialSpy()) {
			var hasSocialSpyPermission = Perms.isOp(player)
                               || Perms.hasAny(player, List.of("chatapi.commands.admin", "chatapi.commands.socialspy"));
			if (!hasSocialSpyPermission) {
				playerManager.update(player, settings -> {
					settings.setEnabledSocialSpy(false);
				});
				return false;
			}
			return true;
		}
		return false;
	}

	public void changeTargetedChannel(ServerPlayerEntity player, Identifier channelId) throws ChatException {
		var targetChannel = channelsById.get(channelId);
		if (targetChannel == null) {
			throw new ChatException("Channel not found");
		}
		playerManager.update(player, settings -> {
			settings.setCurrentChannel(channelId);
		});
	}

	public int getLocalChannelDistance() {
		return modSettings.localChannelDistance();
	}

	private List<ServerPlayerEntity> getPlayers() {
		return minecraftServer.getPlayerManager().getPlayerList();
	}

	public List<PlayerChannelStatus> getPlayerChannelsStatus(ServerPlayerEntity sender) {
		var playerSettings = playerManager.get(sender);
		return channelsById.values().stream()
			.filter(ch -> ch.senderFilter().test(sender))
			.map(ch -> {
				var isCurrent = Objects.equals(ch.id(), playerSettings.getCurrentChannel());
				var isHidden = playerSettings.getHiddenChannels().contains(ch.id());
				return new PlayerChannelStatus(ch, isCurrent, isHidden);
			}).toList();
	}

	public Optional<Channel> getChannel(Identifier channelId) {
		return Optional.ofNullable(channelsById.get(channelId));
	}

	public Collection<Channel> getChannels() {
		return channelsById.values();
	}

	private Channel extractChannelFromMessage(String message) {

		for (Channel channel : channelsById.values()) {
			if (channel.selectorPrefix() != null
			    && message.startsWith(channel.selectorPrefix()) && !message.equals(channel.selectorPrefix())) {
				return channel;
			}
		}

		return null;
	}

	private Channel getPlayerCurrentChannel(ServerPlayerEntity player) {
		var playerSettings = playerManager.get(player);
		var channel = channelsById.get(playerSettings.getCurrentChannel());
		if (channel == null) {
			channel =  channelsById.get(ChatConstants.Ids.Channels.GLOBAL);
		}
		return channel;
	}

	public boolean isChannelHiddenForPlayer(Channel channel, ServerPlayerEntity player) {
		return playerManager.get(player).isChannelHidden(channel);
	}

	public boolean isChannelVisibleForPlayer(Channel channel, ServerPlayerEntity player) {
		return !isChannelHiddenForPlayer(channel, player);
	}

	public boolean toggleHiddenChannelForPlayer(Channel channel, ServerPlayerEntity player) {
		return playerManager.update(player, settings -> {
			return settings.toggleHiddenChannel(channel);
		});
	}

	public void setChannelHiddenForPlayer(Channel channel, ServerPlayerEntity player, boolean hidden) {
		playerManager.update(player, settings -> {
			settings.setHiddenChannel(channel, hidden);
		});
	}

	public void mutePlayerInChannels(ServerPlayerEntity player, Collection<Channel> channelsToMute) {
		playerManager.update(player, settings -> {
			settings.muteChannels(channelsToMute);
		});
	}

	public void unmutePlayerInChannels(ServerPlayerEntity player, Collection<Channel> channels) {
		playerManager.update(player, settings -> {
			settings.unmuteChannels(channels);
		});
	}

	public void enableSocialSpy(ServerPlayerEntity player) {
		playerManager.update(player, settings -> {
			settings.setEnabledSocialSpy(true);
		});
	}

	public void disableSocialSpy(ServerPlayerEntity player) {
		playerManager.update(player, settings -> {
			settings.setEnabledSocialSpy(false);
		});
	}
}
