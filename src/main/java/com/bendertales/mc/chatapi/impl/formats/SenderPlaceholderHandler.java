package com.bendertales.mc.chatapi.impl.formats;

import com.bendertales.mc.chatapi.ChatConstants;
import fr.bendertales.mc.channels.api.AbstractPlaceholderHandler;
import fr.bendertales.mc.channels.api.PlaceholderFormatter;
import fr.bendertales.mc.channels.api.PlaceholderHandler;
import net.minecraft.util.Identifier;


public class SenderPlaceholderHandler extends AbstractPlaceholderHandler
		implements PlaceholderHandler {

	private static final String SENDER_PLACEHOLDER = "%SENDER%";

	public SenderPlaceholderHandler() {
		super(SENDER_PLACEHOLDER);
	}

	@Override
	public PlaceholderFormatter getPlaceholderFormatter() {
		return (message) -> message.sender().getEntityName();
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Formats.SENDER;
	}
}
