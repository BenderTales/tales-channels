package com.bendertales.mc.chatapi.impl.channels;


import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.ChannelDefault;
import com.bendertales.mc.chatapi.impl.ChatManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class GlobalChannel implements ChannelDefault {

	private final ChatManager chatManager;

	public GlobalChannel(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public String getPrefixSelector() {
		return "!";
	}

	@Override
	public String getDefaultFormat() {
		return "[!]%PLAYER_NAME%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getSenderFilter() {
		return (player) -> true;
	}

	@Override
	public Predicate<ServerPlayerEntity> getRecipientsFilter() {
		return (player) -> true;
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.GLOBAL;
	}
}
