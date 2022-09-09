package fr.bendertales.mc.chatapi.impl.formats;

import fr.bendertales.mc.channels.api.AbstractPlaceholderHandler;
import fr.bendertales.mc.channels.api.Message;
import fr.bendertales.mc.channels.api.PlaceholderFormatter;
import fr.bendertales.mc.channels.api.PlaceholderHandler;
import fr.bendertales.mc.chatapi.ChatConstants;
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
