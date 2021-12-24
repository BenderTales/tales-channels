package com.bendertales.mc.chatapi.impl.formats;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.MessageFormatter;
import com.bendertales.mc.chatapi.api.PlaceholderHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class MessagePlaceholderHandler implements PlaceholderHandler {

	private static final String MESSAGE_PLACEHOLDER = "%MESSAGE%";

	@Override
	public int getDefaultPriorityOrder() {
		return 99;
	}

	@Override
	public boolean shouldApplyFormat(String format) {
		return format.contains(MESSAGE_PLACEHOLDER);
	}

	@Override
	public MessageFormatter getMessageFormatter() {
		return (String format, ServerPlayerEntity player, String message) -> format.replace(MESSAGE_PLACEHOLDER, message);
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Formats.MESSAGE;
	}
}
