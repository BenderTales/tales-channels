package com.bendertales.mc.chatapi.impl;

import java.util.*;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.*;
import com.bendertales.mc.chatapi.impl.helper.Perms;
import com.bendertales.mc.chatapi.impl.vo.Channel;
import com.bendertales.mc.chatapi.impl.vo.PlayerChannelStatus;
import com.bendertales.mc.chatapi.impl.vo.Settings;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class ChatManager implements MessageSender {

	private static final ChatManager instance = new ChatManager();

	public static ChatManager get() {
		// Not a big fan of using singleton but necessary for the mixin
		return instance;
	}

	private final ModConfigurationManager configurationManager = new ModConfigurationManager();
	private final PlayerConfigurationManager playerConfigurationManager = new PlayerConfigurationManager();

	private Settings settings;
	private Map<Identifier, Channel>         channelsById;
	private MinecraftServer minecraftServer;

	public void reload() {
		playerConfigurationManager.clearSettings();
		load();
	}

	public void load() {
		this.settings = configurationManager.buildConfiguredChannels();
		this.channelsById = settings.channels();

		var channel = channelsById.get(settings.defaultChannel());
		if (channel == null) {
			channel = channelsById.get(ChatConstants.Ids.Channels.GLOBAL);
			if (channel == null) {
				channel = channelsById.values().stream().findFirst().orElse(null);
			}
		}

		this.playerConfigurationManager.setDefaultChannel(channel == null ? null : channel.id());
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

	private void sendMessage(ServerPlayerEntity sender, String message, Channel channel) throws ChatException {
		ensureSenderIsAllowedInChannel(sender, channel);

		String formattedMessage = channel.formatMessage(sender, message);
		var messageToSend = Text.of(formattedMessage);
		minecraftServer.sendSystemMessage(messageToSend, sender.getUuid());
		getPlayers().stream()
            .filter(p -> isChannelVisibleForPlayer(channel, p))
	        .filter(r -> {
		        var options = new RecipientFilterOptions(hasEnabledSocialSpy(r));
		        return channel.recipientsFilter().filterRecipient(sender, r, options);
	        })
			.forEach(recipient -> recipient.sendMessage(messageToSend, MessageType.CHAT, sender.getUuid()));
	}

	private void ensureSenderIsAllowedInChannel(ServerPlayerEntity sender, Channel channel) throws ChatException {
		if (!channel.senderFilter().test(sender)) {
			throw new ChatException("You cannot send a message in this channel.");
		}

		if (playerConfigurationManager.isPlayerMutedInChannels(sender, channel)) {
			throw new ChatException("You are muted in this channel.");
		}
	}

	private boolean hasEnabledSocialSpy(ServerPlayerEntity player) {
		if (playerConfigurationManager.hasPlayerEnabledSocialSpy(player)) {
			var hasSocialSpyPermission = Perms.isOp(player)
                               || Perms.hasAny(player, List.of("chatapi.commands.admin", "chatapi.commands.socialspy"));
			if (!hasSocialSpyPermission) {
				playerConfigurationManager.disableSocialSpy(player);
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

		playerConfigurationManager.changeActiveChannel(player, targetChannel);
	}

	public int getLocalChannelDistance() {
		return settings.localChannelDistance();
	}

	private List<ServerPlayerEntity> getPlayers() {
		return minecraftServer.getPlayerManager().getPlayerList();
	}

	public List<PlayerChannelStatus> getPlayerChannelsStatus(ServerPlayerEntity sender) {
		var playerSettings = playerConfigurationManager.getOrCreatePlayerSettings(sender);
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
		var playerSettings = playerConfigurationManager.getOrCreatePlayerSettings(player);
		var channel = channelsById.get(playerSettings.getCurrentChannel());
		if (channel == null) {
			channel =  channelsById.get(ChatConstants.Ids.Channels.GLOBAL);
		}
		return channel;
	}

	public boolean isChannelHiddenForPlayer(Channel channel, ServerPlayerEntity player) {
		return playerConfigurationManager.isChannelHiddenForPlayer(channel, player);
	}

	public boolean isChannelVisibleForPlayer(Channel channel, ServerPlayerEntity player) {
		return !isChannelHiddenForPlayer(channel, player);
	}

	public boolean toggleHiddenChannelForPlayer(Channel channel, ServerPlayerEntity player) {
		return playerConfigurationManager.toggleHiddenChannelForPlayer(channel, player);
	}

	private ChatManager() {
	}

	public void mutePlayerInChannels(ServerPlayerEntity player, Collection<Channel> channelsToMute) {
		playerConfigurationManager.mutePlayerInChannels(player, channelsToMute);
	}

	public void unmutePlayerInChannels(ServerPlayerEntity player, Collection<Channel> channels) {
		playerConfigurationManager.unmutePlayerInChannels(player, channels);
	}

	public void enableSocialSpy(ServerPlayerEntity player) {
		playerConfigurationManager.enableSocialSpy(player);
	}

	public void disableSocialSpy(ServerPlayerEntity player) {
		playerConfigurationManager.disableSocialSpy(player);
	}
}
