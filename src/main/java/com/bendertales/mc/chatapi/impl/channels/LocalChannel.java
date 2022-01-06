package com.bendertales.mc.chatapi.impl.channels;

import java.util.function.Predicate;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.ChannelDefault;
import com.bendertales.mc.chatapi.api.RecipientFilter;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class LocalChannel implements ChannelDefault {

	@Override
	public String getPrefixSelector() {
		return ".";
	}

	@Override
	public String getDefaultFormat() {
		return "[L]%PLAYER_NAME%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getSenderFilter() {
		return (p) -> true;
	}

	@Override
	public RecipientFilter getRecipientsFilter() {
		return (sender, recipient, options) -> sender.equals(recipient)
		    || options.isRecipientSocialSpy()
            || (sender.getWorld().equals(recipient.getWorld())
			&& sender.getBlockPos().isWithinDistance(recipient.getBlockPos(), 48.));
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Channels.LOCAL;
	}
}
