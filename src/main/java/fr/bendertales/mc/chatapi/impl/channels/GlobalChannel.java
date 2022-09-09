package fr.bendertales.mc.chatapi.impl.channels;


import java.util.function.Predicate;

import fr.bendertales.mc.channels.api.MessageVisibility;
import fr.bendertales.mc.channels.api.ModChannelImplementationsProvider;
import fr.bendertales.mc.channels.api.RecipientFilter;
import fr.bendertales.mc.chatapi.ChatConstants;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class GlobalChannel implements ModChannelImplementationsProvider {

	public static final String PERMISSION = "chatapi.channels.global";

	@Override
	public String getPrefixSelector() {
		return "!";
	}

	@Override
	public String getDefaultFormat() {
		return "[!]%SENDER%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getSenderFilter() {
		return (player) -> true;
	}

	@Override
	public RecipientFilter getRecipientsFilter() {
		/* return (sender, recipient) -> sender.equals(recipient)
            || Perms.isOp(recipient)
            || Perms.hasAny(recipient, singleton(PERMISSION));*/

		return (sender, recipient, options) -> MessageVisibility.SHOW;
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.GLOBAL;
	}
}
