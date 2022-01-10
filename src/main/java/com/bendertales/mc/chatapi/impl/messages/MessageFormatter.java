package com.bendertales.mc.chatapi.impl.messages;

import java.util.ArrayList;
import java.util.Collection;

import com.bendertales.mc.chatapi.api.Message;
import com.bendertales.mc.chatapi.api.PlaceholderFormatter;
import com.bendertales.mc.chatapi.api.SpecificToRecipientPlaceholderFormatter;
import com.bendertales.mc.chatapi.impl.vo.Placeholder;


public class MessageFormatter {

	private final String                     initialFormat;
	private final ArrayList<PlaceholderFormatter> placeholderFormatters;
	private final ArrayList<SpecificToRecipientPlaceholderFormatter> recipientPlaceholderFormatters = new ArrayList<>(2);

	public MessageFormatter(String initialFormat, Collection<Placeholder> placeholders) {
		this.initialFormat = initialFormat;
		placeholderFormatters = new ArrayList<>(placeholders.size());

		for (Placeholder placeholder : placeholders) {
			if (placeholder.recipientPlaceholderFormatter() != null) {
				recipientPlaceholderFormatters.add(placeholder.recipientPlaceholderFormatter());
			}
			if (placeholder.placeholderFormatter() != null) {
				placeholderFormatters.add(placeholder.placeholderFormatter());
			}
		}

		placeholderFormatters.trimToSize();
		recipientPlaceholderFormatters.trimToSize();
	}

	public FormattedMessage prepare(Message message) {
		var line = initialFormat;

		for (PlaceholderFormatter formatter : placeholderFormatters) {
			line = formatter.formatMessage(line, message);
		}

		if (recipientPlaceholderFormatters.isEmpty()) {
			return new DefinitiveFormattedMessage(line);
		}

		return new PerRecipientFormattedMessage(message, recipientPlaceholderFormatters, line);
	}

}
