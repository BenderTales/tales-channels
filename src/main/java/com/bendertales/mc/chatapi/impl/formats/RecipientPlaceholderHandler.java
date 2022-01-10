package com.bendertales.mc.chatapi.impl.formats;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.PlaceholderFormatter;
import com.bendertales.mc.chatapi.api.PlaceholderHandler;
import com.bendertales.mc.chatapi.api.SpecificToRecipientPlaceholderFormatter;
import net.minecraft.util.Identifier;


public class RecipientPlaceholderHandler implements PlaceholderHandler {

	private static final String RECIPIENT_PLACEHOLDER = "%RECIPIENT%";

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Formats.RECIPIENT;
	}

	@Override
	public int getDefaultPriorityOrder() {
		return -1;
	}

	@Override
	public boolean shouldApplyFormat(String format) {
		return format.contains(RECIPIENT_PLACEHOLDER);
	}

	@Override
	public PlaceholderFormatter getPlaceholderFormatter() {
		return null;
	}

	@Override
	public SpecificToRecipientPlaceholderFormatter getSpecificToRecipientPlaceholderFormatter() {
		return (line, message, recipient) -> {
			String recipientName = recipient == null ? "CONSOLE" : recipient.getEntityName();
			return line.replace(RECIPIENT_PLACEHOLDER, recipientName);
		};
	}
}
