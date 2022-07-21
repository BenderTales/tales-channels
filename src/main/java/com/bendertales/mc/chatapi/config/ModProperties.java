package com.bendertales.mc.chatapi.config;

import java.util.Map;

import net.minecraft.util.Identifier;


public class ModProperties {

	private Identifier        defaultChannel;
	private int localChannelDistance;

	private PrivateMessageProperties privateMessages;

	private Map<Identifier, PlaceholderProperties> placeholders;
	private Map<Identifier, ChannelProperties>     channels;

	public Identifier getDefaultChannel() {
		return defaultChannel;
	}

	public void setDefaultChannel(Identifier defaultChannel) {
		this.defaultChannel = defaultChannel;
	}

	public int getLocalChannelDistance() {
		return localChannelDistance;
	}

	public void setLocalChannelDistance(int localChannelDistance) {
		this.localChannelDistance = localChannelDistance;
	}

	public PrivateMessageProperties getPrivateMessages() {
		return privateMessages;
	}

	public void setPrivateMessages(PrivateMessageProperties privateMessages) {
		this.privateMessages = privateMessages;
	}

	public Map<Identifier, PlaceholderProperties> getPlaceholders() {
		return placeholders;
	}

	public void setPlaceholders(Map<Identifier, PlaceholderProperties> placeholders) {
		this.placeholders = placeholders;
	}

	public Map<Identifier, ChannelProperties> getChannels() {
		return channels;
	}

	public void setChannels(Map<Identifier, ChannelProperties> channels) {
		this.channels = channels;
	}
}
