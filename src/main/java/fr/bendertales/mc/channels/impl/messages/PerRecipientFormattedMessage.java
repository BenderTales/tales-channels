package fr.bendertales.mc.channels.impl.messages;

import java.util.List;

import fr.bendertales.mc.channels.api.Message;
import fr.bendertales.mc.channels.impl.vo.MessageOptions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


public class PerRecipientFormattedMessage implements FormattedMessage {

	private final Message                                    message;
	private final List<IndexedRecipientPlaceholderFormatter> recipientPlaceholderFormatters;
	private final String                                 line;

	public PerRecipientFormattedMessage(Message message,
	                                    List<IndexedRecipientPlaceholderFormatter> recipientPlaceholderFormatters,
	                                    String line) {
		this.message = message;
		this.recipientPlaceholderFormatters = recipientPlaceholderFormatters;
		this.line = line;
	}


	@Override
	public Text forRecipient(ServerPlayerEntity recipient, MessageOptions options) {
		var line = new StringBuilder(this.line);

		int index = 0;
		for (var rpf : recipientPlaceholderFormatters) {
			var key = rpf.key();
			index = line.indexOf(key, index);
			var newContent = rpf.formatter().format(message, recipient);
			line.replace(index, rpf.length(), newContent);
			index += newContent.length();
		}

		if (options.socialSpy()) {
			line.insert(0, "§m*§r");
		}

		return Text.of(line.toString());
	}
}
