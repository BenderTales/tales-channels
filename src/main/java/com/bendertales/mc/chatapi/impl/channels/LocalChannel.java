package com.bendertales.mc.chatapi.impl.channels;

import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.Channel;
import com.bendertales.mc.chatapi.impl.ChatManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class LocalChannel implements Channel {

	private final ChatManager chatManager;

	public LocalChannel(ChatManager chatManager) {
		this.chatManager = chatManager;
	}


	@Override
	public String getDefaultFormat() {
		return "[L]%PLAYER_NAME%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getChannelUsabilityFilter() {
		return (p) -> true;
	}

	@Override
	public Predicate<ServerPlayerEntity> getChannelRecipientFilter() {
		return (p) -> true;
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.LOCAL;
	}
}
