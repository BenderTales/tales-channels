package com.bendertales.mc.chatapi.impl.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.util.Identifier;


public class PlayerSettings {

	private final UUID playerUuid;
	private final List<Identifier> hiddenChannels = new ArrayList<>();
	private Channel currentChannel;

	public PlayerSettings(UUID playerUuid) {
		this.playerUuid = playerUuid;
	}

	public UUID getPlayerUuid() {
		return playerUuid;
	}

	public List<Identifier> getHiddenChannels() {
		return hiddenChannels;
	}

	public Channel getCurrentChannel() {
		return currentChannel;
	}

	public void setCurrentChannel(Channel currentChannel) {
		this.currentChannel = currentChannel;
	}

	public boolean isChannelHidden(Channel channel) {
		return hiddenChannels.contains(channel.id());
	}

	public boolean toggleHiddenChannel(Channel channel) {
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
}
