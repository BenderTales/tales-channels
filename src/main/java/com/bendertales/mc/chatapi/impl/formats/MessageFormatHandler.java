package com.bendertales.mc.chatapi.impl.formats;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.FormatHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class MessageFormatHandler implements FormatHandler {

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
	public String handleMessage(String format, ServerPlayerEntity player, String message) {
		return format.replace(MESSAGE_PLACEHOLDER, message);
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Formats.MESSAGE;
	}
}
