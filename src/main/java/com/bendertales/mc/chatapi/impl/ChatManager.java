package com.bendertales.mc.chatapi.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.bendertales.mc.chatapi.api.ChannelDefault;
import com.bendertales.mc.chatapi.api.Registry;
import com.bendertales.mc.chatapi.impl.vo.Channel;
import com.bendertales.mc.chatapi.impl.vo.Placeholder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class ChatManager {

	private final Map<UUID, Set<Identifier>> hiddenChannelsByPlayer = new HashMap<>();
	private Map<Identifier, Channel>         channelsById;

	public void reload() {
		hiddenChannelsByPlayer.clear();
		load();
	}

	public void load() {
		this.channelsById = buildConfiguredChannels();
	}

	public boolean isChannelHiddenForPlayer(Channel channel, ServerPlayerEntity player) {
		var channels = hiddenChannelsByPlayer.get(player.getUuid());
		if (channels == null) {
			return false;
		}
		return channels.contains(channel.id());
	}

	public boolean toggleHiddenChannelForPlayer(Channel channel, ServerPlayerEntity player) {
		var hiddenChannels = hiddenChannelsByPlayer.computeIfAbsent(player.getUuid(), u -> new HashSet<>());
		var channelId = channel.id();
		if (hiddenChannels.contains(channelId)) {
			hiddenChannels.remove(channelId);
			return false;
		}
		else {
			hiddenChannels.add(channelId);
			return true;
		}
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

			var channel = new Channel(channelDefault.getId(), format, channelPlaceholders,
			                          channelDefault.getRecipientsFilter(), channelDefault.getSenderFilter());

			channelsById.put(channelDefault.getId(), channel);
		}

		return channelsById;
	}

}
