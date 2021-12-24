package com.bendertales.mc.chatapi.impl.channels;

import java.util.Collections;
import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.ChannelDefault;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.helper.Perms;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static java.util.Collections.singleton;


public class ModerationChannel implements ChannelDefault {

	private final ChatManager chatManager;

	public ModerationChannel(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public String getDefaultFormat() {
		return "[MOD]%PLAYER_NAME%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getSenderFilter() {
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, singleton("chat.channel.moderation"));
	}

	@Override
	public Predicate<ServerPlayerEntity> getRecipientsFilter() {
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, singleton("chat.channel.moderation"));
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.MODO;
	}
}
