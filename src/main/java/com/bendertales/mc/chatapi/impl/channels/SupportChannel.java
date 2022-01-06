package com.bendertales.mc.chatapi.impl.channels;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.ChannelDefault;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.helper.Perms;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class SupportChannel implements ChannelDefault {

	public static final String SEND_PERMISSION = "chatapi.channels.support.send";
	public static final String READ_PERMISSION = "chatapi.channels.support.read";

	@Override
	public String getPrefixSelector() {
		return "?";
	}

	@Override
	public String getDefaultFormat() {
		return "[SUPPORT]%PLAYER_NAME%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getSenderFilter() {
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, List.of(SEND_PERMISSION));
	}

	@Override
	public BiFunction<ServerPlayerEntity, ServerPlayerEntity, Boolean> getRecipientsFilter() {
		return (sender, player) -> sender.equals(player)
            || Perms.isOp(player)
            || Perms.hasAny(player, List.of(READ_PERMISSION));
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.SUPPORT;
	}
}
