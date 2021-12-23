package com.bendertales.mc.chatapi.impl.channels;

import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.Channel;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.helper.Perms;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class HelpersChannel implements Channel {

	private final ChatManager chatManager;

	public HelpersChannel(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public String getDefaultFormat() {
		return "[Help]%PLAYER_NAME%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getChannelUsabilityFilter() {
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, "chat.channel.helpers");
	}

	@Override
	public Predicate<ServerPlayerEntity> getChannelRecipientFilter() {
		//TODO : Check channel is not muted
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, "chat.channel.helpers");
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.HELPERS;
	}
}
