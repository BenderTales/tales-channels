package com.bendertales.mc.chatapi.config;

import java.util.Map;

import net.minecraft.util.Identifier;


public class ModConfiguration {

	private Identifier defaultChannel;
	private ChannelConfiguration privateMessageConfiguration;

	private Map<Identifier, FormatConfiguration> formats;
	private Map<Identifier, ChannelConfiguration> channels;

	public Identifier getDefaultChannel() {
		return defaultChannel;
	}

	public void setDefaultChannel(Identifier defaultChannel) {
		this.defaultChannel = defaultChannel;
	}

	public ChannelConfiguration getPrivateMessageConfiguration() {
		return privateMessageConfiguration;
	}

	public void setPrivateMessageConfiguration(ChannelConfiguration privateMessageConfiguration) {
		this.privateMessageConfiguration = privateMessageConfiguration;
	}

	public Map<Identifier, FormatConfiguration> getFormats() {
		return formats;
	}

	public void setFormats(Map<Identifier, FormatConfiguration> formats) {
		this.formats = formats;
	}

	public Map<Identifier, ChannelConfiguration> getChannels() {
		return channels;
	}

	public void setChannels(Map<Identifier, ChannelConfiguration> channels) {
		this.channels = channels;
	}
}
