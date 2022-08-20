package com.bendertales.mc.chatapi.impl.messages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.bendertales.mc.chatapi.api.Message;
import com.bendertales.mc.chatapi.api.PlaceholderHandler;


public class MessageFormatter {

	private final String                                 initialFormat;
	private final List<IndexedPlaceholderFormatter>          placeholderFormatters;
	private final List<IndexedRecipientPlaceholderFormatter> recipientPlaceholderFormatters;

	public MessageFormatter(String initialFormat, List<PlaceholderHandler> placeholders) {
		this.initialFormat = initialFormat;

		var indexedPlaceholderFormatters = new ArrayList<IndexedPlaceholderFormatter>();
		var indexedRecipientPlaceholderFormatters = new ArrayList<IndexedRecipientPlaceholderFormatter>();

		for (var placeholderHandler : placeholders) {
			var placeholder = placeholderHandler.getPlaceholder();
			int index = initialFormat.indexOf(placeholder);
			if (index != -1) {
				var rpf = placeholderHandler.getPerRecipientPlaceholderFormatter();
				if (rpf != null) {
					indexedRecipientPlaceholderFormatters.add(
						new IndexedRecipientPlaceholderFormatter(placeholder, rpf, index, placeholder.length())
					);
				}

				var pf = placeholderHandler.getPlaceholderFormatter();
				if (pf != null) {
					indexedPlaceholderFormatters.add(
						new IndexedPlaceholderFormatter(placeholder, pf, index, placeholder.length())
					);
				}
			}
		}

		placeholderFormatters =
				indexedPlaceholderFormatters.stream()
                    .sorted(Comparator.comparingInt(IndexedPlaceholderFormatter::index))
                    .toList();
		recipientPlaceholderFormatters =
				indexedRecipientPlaceholderFormatters.stream()
                    .sorted(Comparator.comparingInt(IndexedRecipientPlaceholderFormatter::index))
                    .toList();
	}

	public FormattedMessage prepare(Message message) {
		var line = new StringBuilder(initialFormat);

		int index = 0;
		for (var pf : placeholderFormatters) {
			var key = pf.key();
			index = line.indexOf(key, index);
			var newContent = pf.formatter().format(message);
			line.replace(index, index + pf.length(), newContent);
			index += newContent.length();
		}

		if (recipientPlaceholderFormatters.isEmpty()) {
			return new DefinitiveFormattedMessage(line.toString());
		}

		return new PerRecipientFormattedMessage(message, recipientPlaceholderFormatters, line.toString());
	}
}
