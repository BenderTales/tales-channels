package com.bendertales.mc.chatapi.impl.vo;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.util.Identifier;


public class PlayerSettings {

	private final ObjectSet<Identifier> hiddenChannels = new ObjectOpenHashSet<>();
	private final ObjectSet<Identifier> mutedChannels = new ObjectOpenHashSet<>();
	private       Identifier            currentChannel;
	private       boolean enabledSocialSpy = false;
	private       UUID lastMessageSender = null;

	public PlayerSettings() {
	}

	public UUID getLastMessageSender() {
		return lastMessageSender;
	}

	public void setLastMessageSender(UUID lastMessageSender) {
		this.lastMessageSender = lastMessageSender;
	}

	public Set<Identifier> getHiddenChannels() {
		return hiddenChannels;
	}

	public Identifier getCurrentChannel() {
		return currentChannel;
	}

	public void setCurrentChannel(Identifier currentChannel) {
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

	public void muteChannels(Collection<Channel> channels) {
		channels.stream().map(Channel::id).forEach(mutedChannels::add);
	}

	public void muteChannelsById(Collection<Identifier> channels) {
		mutedChannels.addAll(channels);
	}

	public boolean isMutedInChannel(Channel channel) {
		return mutedChannels.contains(channel.id());
	}

	public Set<Identifier> getMutedChannels() {
		return mutedChannels;
	}

	public void unmuteChannels(Collection<Channel> channels) {
		channels.stream().map(Channel::id).forEach(mutedChannels::remove);
	}

	public boolean isEnabledSocialSpy() {
		return enabledSocialSpy;
	}

	public void setEnabledSocialSpy(boolean enabledSocialSpy) {
		this.enabledSocialSpy = enabledSocialSpy;
	}


}
