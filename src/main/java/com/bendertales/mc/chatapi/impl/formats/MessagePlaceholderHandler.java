package com.bendertales.mc.chatapi.impl.formats;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.AbstractPlaceholderHandler;
import com.bendertales.mc.chatapi.api.Message;
import com.bendertales.mc.chatapi.api.PlaceholderFormatter;
import com.bendertales.mc.chatapi.api.PlaceholderHandler;
import net.minecraft.util.Identifier;


public class MessagePlaceholderHandler extends AbstractPlaceholderHandler
		implements PlaceholderHandler {

	private static final String MESSAGE_PLACEHOLDER = "%MESSAGE%";

	public MessagePlaceholderHandler() {
		super(MESSAGE_PLACEHOLDER);
	}

	@Override
	public PlaceholderFormatter getPlaceholderFormatter() {
		return Message::content;
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Formats.MESSAGE;
	}
}
