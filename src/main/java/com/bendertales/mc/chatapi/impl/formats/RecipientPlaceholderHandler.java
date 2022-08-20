package com.bendertales.mc.chatapi.impl.formats;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.AbstractPlaceholderHandler;
import com.bendertales.mc.chatapi.api.PerRecipientPlaceholderFormatter;
import com.bendertales.mc.chatapi.api.PlaceholderFormatter;
import com.bendertales.mc.chatapi.api.PlaceholderHandler;
import net.minecraft.util.Identifier;


public class RecipientPlaceholderHandler extends AbstractPlaceholderHandler
		implements PlaceholderHandler {

	private static final String RECIPIENT_PLACEHOLDER = "%RECIPIENT%";

	public RecipientPlaceholderHandler() {
		super(RECIPIENT_PLACEHOLDER);
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Formats.RECIPIENT;
	}

	@Override
	public PlaceholderFormatter getPlaceholderFormatter() {
		return null;
	}

	@Override
	public PerRecipientPlaceholderFormatter getPerRecipientPlaceholderFormatter() {
		return (message, recipient) -> recipient == null ? "CONSOLE" : recipient.getEntityName();
	}
}
