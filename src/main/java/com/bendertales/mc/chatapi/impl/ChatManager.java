package com.bendertales.mc.chatapi.impl;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.ChannelDefault;
import com.bendertales.mc.chatapi.api.ChatException;
import com.bendertales.mc.chatapi.api.MessageSender;
import com.bendertales.mc.chatapi.api.Registry;
import com.bendertales.mc.chatapi.impl.vo.Channel;
import com.bendertales.mc.chatapi.impl.vo.Placeholder;
import com.bendertales.mc.chatapi.impl.vo.PlayerChannelStatus;
import com.bendertales.mc.chatapi.impl.vo.PlayerSettings;
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

	private final Map<UUID, PlayerSettings>  playersSettingsById    = new HashMap<>();
	private Map<Identifier, Channel>         channelsById;

	private MinecraftServer minecraftServer;

	public void reload() {
		load();
	}

	public void load() {
		//TODO load player settings
		this.channelsById = buildConfiguredChannels();
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
			throw new ChatException("chat.channel.not_found");
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
	        .filter(r -> channel.recipientsFilter().apply(sender, r))
			.forEach(recipient -> {
				recipient.sendMessage(messageToSend, MessageType.CHAT, sender.getUuid());
			});
	}

	private void ensureSenderIsAllowedInChannel(ServerPlayerEntity sender, Channel channel) throws ChatException {
		if (!channel.senderFilter().test(sender)) {
			throw new ChatException("§4You cannot send a message in this channel.");
		}

		var senderSettings = getOrCreatePlayerSettings(sender);
		if (senderSettings.isMutedInChannel(channel)) {
			throw new ChatException("§4You are muted in this channel.");
		}
	}

	public void changeTargetedChannel(ServerPlayerEntity player, Identifier channelId) throws ChatException {
		var targetChannel = channelsById.get(channelId);
		if (targetChannel == null) {
			throw new ChatException("Channel not found");
		}

		var playerSettings = getOrCreatePlayerSettings(player);
		playerSettings.setCurrentChannel(targetChannel);
		//TODO: save in file
	}

	private List<ServerPlayerEntity> getPlayers() {
		return minecraftServer.getPlayerManager().getPlayerList();
	}

	public List<PlayerChannelStatus> getPlayerChannelsStatus(ServerPlayerEntity sender) {
		var playerSettings = getOrCreatePlayerSettings(sender);
		return channelsById.values().stream()
			.filter(ch -> ch.senderFilter().test(sender))
			.map(ch -> {
				var isCurrent = playerSettings.getCurrentChannel() == ch;
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
		var playerSettings = getOrCreatePlayerSettings(player);
		return playerSettings.getCurrentChannel();
	}

	public boolean isChannelHiddenForPlayer(Channel channel, ServerPlayerEntity player) {
		var playerSettings = getOrCreatePlayerSettings(player);
		return playerSettings.isChannelHidden(channel);
	}

	public boolean isChannelVisibleForPlayer(Channel channel, ServerPlayerEntity player) {
		return !isChannelHiddenForPlayer(channel, player);
	}

	public boolean toggleHiddenChannelForPlayer(Channel channel, ServerPlayerEntity player) {
		var playerSettings = getOrCreatePlayerSettings(player);
		return playerSettings.toggleHiddenChannel(channel);
	}

	private PlayerSettings getOrCreatePlayerSettings(ServerPlayerEntity player) {
		return playersSettingsById.computeIfAbsent(player.getUuid(), id -> {
			var settings = new PlayerSettings(id);
			settings.setCurrentChannel(channelsById.get(ChatConstants.Ids.Channels.GLOBAL));
			return settings;
		});
	}

	private HashMap<Identifier, Channel> buildConfiguredChannels() {
		var placeholdersById = Registry.FORMAT_HANDLERS.stream()
            .map(ph -> new Placeholder(ph.getId(), ph.getDefaultPriorityOrder(), ph.getMessageFormatter()))
            .collect(Collectors.toMap(Placeholder::id, p -> p));

		var channelsById = new HashMap<Identifier, Channel>();

		for (ChannelDefault channelDefault : Registry.CHANNEL_HANDLERS) {
			var format = channelDefault.getDefaultFormat();

			var channelPlaceholders = Registry.FORMAT_HANDLERS.stream()
                .filter(ph -> ph.shouldApplyFormat(format))
                .map(ph -> placeholdersById.get(ph.getId()))
                .sorted(Comparator.comparingInt(Placeholder::applyOrder))
                .toList();

			var channel = new Channel(channelDefault.getId(), channelDefault.getPrefixSelector(),
			                          format, channelPlaceholders,
			                          channelDefault.getRecipientsFilter(), channelDefault.getSenderFilter());

			channelsById.put(channelDefault.getId(), channel);
		}

		return channelsById;
	}

	private ChatManager() {
	}

	public void mutePlayerInChannels(ServerPlayerEntity player, Collection<Channel> channelsToMute) {
		var playerSettings = getOrCreatePlayerSettings(player);
		playerSettings.muteChannels(channelsToMute);
	}

	public void unmutePlayerInChannels(ServerPlayerEntity player, Collection<Channel> channels) {
		var playerSettings = getOrCreatePlayerSettings(player);
		playerSettings.unmuteChannels(channels);
	}
}
