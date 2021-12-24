package com.bendertales.mc.chatapi.impl.channels;

import java.util.List;
import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.ChannelDefault;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.helper.Perms;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class SupportChannel implements ChannelDefault {

	private final ChatManager chatManager;

	public SupportChannel(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public String getDefaultFormat() {
		return "[SUPPORT]%PLAYER_NAME%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getSenderFilter() {
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, List.of("chat.channel.support.send"));
	}

	@Override
	public Predicate<ServerPlayerEntity> getRecipientsFilter() {
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, List.of("chat.channel.support.read"));
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.SUPPORT;
	}
}
