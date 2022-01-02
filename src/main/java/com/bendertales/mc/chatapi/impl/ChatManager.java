package com.bendertales.mc.chatapi.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.ChannelDefault;
import com.bendertales.mc.chatapi.api.ChatException;
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
import net.minecraft.util.Util;


public class ChatManager {

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

	public void handleMessage(ServerPlayerEntity sender, String message) {
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

	private void sendMessage(ServerPlayerEntity sender, String message, Channel channel) {
		if (!channel.senderFilter().test(sender)) {
			sender.sendMessage(Text.of("§4You cannot send a message in this channel"), MessageType.CHAT, Util.NIL_UUID);
			return;
		}

		String formattedMessage = channel.formatMessage(sender, message);
		var messageToSend = Text.of(formattedMessage);
		minecraftServer.sendSystemMessage(messageToSend, sender.getUuid());
		getPlayers().stream()
	        .filter(channel.recipientsFilter())
			.forEach(recipient -> {
				recipient.sendMessage(messageToSend, MessageType.CHAT, sender.getUuid());
			});
	}

	private List<ServerPlayerEntity> getPlayers() {
		return minecraftServer.getPlayerManager().getPlayerList();
	}

	public List<PlayerChannelStatus> getPlayerChannelsStatus(ServerPlayerEntity player) {
		var playerSettings = getOrCreatePlayerSettings(player);
		return channelsById.values().stream()
			.filter(ch -> ch.recipientsFilter().test(player))
			.map(ch -> {
				var isCurrent = playerSettings.getCurrentChannel() == ch;
				var isHidden = playerSettings.getHiddenChannels().contains(ch.id());
				return new PlayerChannelStatus(ch, isCurrent, isHidden);
			}).toList();
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

	private PlayerSettings getOrCreatePlayerSettings(ServerPlayerEntity player) {
		return playersSettingsById.computeIfAbsent(player.getUuid(), id -> {
			var settings = new PlayerSettings(id);
			settings.setCurrentChannel(channelsById.get(ChatConstants.Ids.Channels.GLOBAL));
			return settings;
		});
	}

	public boolean toggleHiddenChannelForPlayer(Channel channel, ServerPlayerEntity player) {
		var playerSettings = getOrCreatePlayerSettings(player);
		return playerSettings.toggleHiddenChannel(channel);
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
}
