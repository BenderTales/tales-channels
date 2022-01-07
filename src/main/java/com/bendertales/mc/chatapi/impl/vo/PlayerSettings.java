package com.bendertales.mc.chatapi.impl.vo;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.util.Identifier;


public class PlayerSettings {

	private final UUID                  playerUuid;
	private final ObjectSet<Identifier> hiddenChannels = new ObjectOpenHashSet<>();
	private final ObjectSet<Identifier> mutedChannels = new ObjectOpenHashSet<>();
	private       Channel               currentChannel;
	private boolean enabledSocialSpy = false;

	public PlayerSettings(UUID playerUuid) {
		this.playerUuid = playerUuid;
	}

	public UUID getPlayerUuid() {
		return playerUuid;
	}

	public Set<Identifier> getHiddenChannels() {
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

	public void muteChannels(Collection<Channel> channels) {
		channels.stream().map(Channel::id).forEach(mutedChannels::add);
	}

	public boolean isMutedInChannel(Channel channel) {
		return mutedChannels.contains(channel.id());
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
