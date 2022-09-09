package fr.bendertales.mc.chatapi.impl.formats;

import fr.bendertales.mc.channels.api.AbstractPlaceholderHandler;
import fr.bendertales.mc.channels.api.PerRecipientPlaceholderFormatter;
import fr.bendertales.mc.channels.api.PlaceholderFormatter;
import fr.bendertales.mc.channels.api.PlaceholderHandler;
import fr.bendertales.mc.chatapi.ChatConstants;
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
