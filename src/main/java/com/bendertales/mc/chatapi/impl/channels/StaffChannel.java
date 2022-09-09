package com.bendertales.mc.chatapi.impl.channels;

import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import fr.bendertales.mc.channels.api.MessageVisibility;
import fr.bendertales.mc.channels.api.ModChannelImplementationsProvider;
import fr.bendertales.mc.channels.api.RecipientFilter;
import fr.bendertales.mc.talesservercommon.helpers.Perms;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static java.util.Collections.singleton;


public class StaffChannel implements ModChannelImplementationsProvider {

	public static final String PERMISSION = "chatapi.channels.staff";

	@Override
	public String getDefaultFormat() {
		return "[STAFF]%SENDER%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getSenderFilter() {
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, singleton(PERMISSION));
	}

	@Override
	public RecipientFilter getRecipientsFilter() {
		return (sender, player, options) -> {
			if (sender.equals(player)
					|| Perms.isOp(player)
					|| Perms.hasAny(player, singleton(PERMISSION))) {
				return MessageVisibility.SHOW;
			}
			return MessageVisibility.HIDE;
		};
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.STAFF;
	}
}
