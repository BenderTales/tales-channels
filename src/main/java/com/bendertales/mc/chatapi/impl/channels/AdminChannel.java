package com.bendertales.mc.chatapi.impl.channels;

import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.ChannelDefault;
import com.bendertales.mc.chatapi.api.RecipientFilter;
import com.bendertales.mc.chatapi.impl.helper.Perms;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static java.util.Collections.singleton;


public class AdminChannel implements ChannelDefault {

	public static final String PERMISSION = "chatapi.channels.admins";

	@Override
	public String getDefaultFormat() {
		return "[ADMIN]%PLAYER_NAME%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getSenderFilter() {
		return (player -> Perms.isOp(player)
            || Perms.hasAny(player, singleton(PERMISSION)));
	}

	@Override
	public RecipientFilter getRecipientsFilter() {
		return (sender, player, options) -> sender.equals(player)
            || Perms.isOp(player)
            || Perms.hasAny(player, singleton(PERMISSION));
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.ADMIN;
	}
}
