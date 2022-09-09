package com.bendertales.mc.chatapi.impl.channels;

import java.util.List;
import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import fr.bendertales.mc.channels.api.MessageVisibility;
import fr.bendertales.mc.channels.api.ModChannelImplementationsProvider;
import fr.bendertales.mc.channels.api.RecipientFilter;
import fr.bendertales.mc.talesservercommon.helpers.Perms;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class SupportChannel implements ModChannelImplementationsProvider {

	public static final String SEND_PERMISSION = "chatapi.channels.support.send";
	public static final String READ_PERMISSION = "chatapi.channels.support.read";

	@Override
	public String getPrefixSelector() {
		return "?";
	}

	@Override
	public String getDefaultFormat() {
		return "[SUPPORT]%SENDER%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getSenderFilter() {
		return (player) -> Perms.isOp(player) || Perms.hasAny(player, List.of(SEND_PERMISSION));
	}

	@Override
	public RecipientFilter getRecipientsFilter() {
		return (sender, player, options) -> {
			if (sender.equals(player)
					|| Perms.isOp(player)
					|| Perms.hasAny(player, List.of(READ_PERMISSION))) {
				return MessageVisibility.SHOW;
			}
			return MessageVisibility.HIDE;
		};
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.SUPPORT;
	}
}
