package com.bendertales.mc.chatapi.impl.vo;

import java.util.Collection;
import java.util.Set;

import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.nbt.*;
import net.minecraft.util.Identifier;


public class ChatApiPlayerSettings {

	private final ObjectSet<Identifier> hiddenChannels = new ObjectOpenHashSet<>();
	private final ObjectSet<Identifier> mutedChannels = new ObjectOpenHashSet<>();
	private       Identifier               currentChannel;
	private boolean enabledSocialSpy = false;

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

	public void writeDataToNbt(NbtCompound chatCompound) {
		// Not saving hidden channels to prevent players mistakes
		chatCompound.putString("current-channel", currentChannel.toString());
		chatCompound.putBoolean("social-spy", enabledSocialSpy);

		NbtList mutedChannelsNbt = new NbtList();
		mutedChannels.forEach(ch -> {
			mutedChannelsNbt.add(NbtString.of(ch.toString()));
		});
		chatCompound.put("muted-channels", mutedChannelsNbt);
	}

	public void readDataFromNbt(NbtCompound chatCompound) {
		var channelIdStr = chatCompound.getString("current-channel");
		this.currentChannel = new Identifier(channelIdStr);
		this.enabledSocialSpy = chatCompound.getBoolean("social-spy");

		var mutedChannelsNbt = chatCompound.getList("muted-channels", 8);
		mutedChannelsNbt.forEach(chEl -> {
			mutedChannels.add(new Identifier(chEl.asString()));
		});
	}
}
