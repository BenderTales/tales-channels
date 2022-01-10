package com.bendertales.mc.chatapi.impl.formats;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.PlaceholderFormatter;
import com.bendertales.mc.chatapi.api.PlaceholderHandler;
import net.minecraft.util.Identifier;


public class SenderPlaceholderHandler implements PlaceholderHandler {

	private static final String SENDER_PLACEHOLDER = "%SENDER%";

	@Override
	public int getDefaultPriorityOrder() {
		return -1;
	}

	@Override
	public boolean shouldApplyFormat(String format) {
		return format.contains(SENDER_PLACEHOLDER);
	}

	@Override
	public PlaceholderFormatter getPlaceholderFormatter() {
		return (format, message) -> format.replace(SENDER_PLACEHOLDER, message.sender().getEntityName());
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Formats.SENDER;
	}
}
