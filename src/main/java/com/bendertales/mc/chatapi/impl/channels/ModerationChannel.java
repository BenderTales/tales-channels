package com.bendertales.mc.chatapi.impl.channels;

import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.Channel;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.helper.Perms;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class ModerationChannel implements Channel {

	private final ChatManager chatManager;

	public ModerationChannel(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public String getDefaultFormat() {
		return "[MOD]%PLAYER_NAME%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getChannelUsabilityFilter() {
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, "chat.channel.moderation");
	}

	@Override
	public Predicate<ServerPlayerEntity> getChannelRecipientFilter() {
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, "chat.channel.moderation");
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.MODO;
	}
}
