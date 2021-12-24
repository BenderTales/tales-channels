package com.bendertales.mc.chatapi.impl.channels;

import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.ChannelDefault;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.helper.Perms;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static java.util.Collections.singleton;


public class HelpersChannel implements ChannelDefault {

	private final ChatManager chatManager;

	public HelpersChannel(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public String getDefaultFormat() {
		return "[Help]%PLAYER_NAME%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getSenderFilter() {
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, singleton("chat.channel.helpers"));
	}

	@Override
	public Predicate<ServerPlayerEntity> getRecipientsFilter() {
		//TODO : Check channel is not muted
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, singleton("chat.channel.helpers"));
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.HELPERS;
	}
}
