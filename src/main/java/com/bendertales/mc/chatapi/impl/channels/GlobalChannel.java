package com.bendertales.mc.chatapi.impl.channels;


import java.util.function.BiFunction;
import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.ChannelDefault;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class GlobalChannel implements ChannelDefault {

	public static final String PERMISSION = "chatapi.channels.global";

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
		/* return (player) -> Perms.isOp(player)
            || Perms.hasAny(player, singleton(PERMISSION)); */

		return (player) -> true;
	}

	@Override
	public BiFunction<ServerPlayerEntity, ServerPlayerEntity, Boolean> getRecipientsFilter() {
		/* return (sender, recipient) -> sender.equals(recipient)
            || Perms.isOp(recipient)
            || Perms.hasAny(recipient, singleton(PERMISSION));*/

		return (sender, recipient) -> true;
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.GLOBAL;
	}
}
