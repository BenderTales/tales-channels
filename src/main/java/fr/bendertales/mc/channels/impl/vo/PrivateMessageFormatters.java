package fr.bendertales.mc.channels.impl.vo;

import fr.bendertales.mc.channels.impl.messages.MessageFormatter;


public record PrivateMessageFormatters(
		MessageFormatter consoleFormatter,
		MessageFormatter senderIsYouFormatter,
		MessageFormatter senderIsOtherFormatter
) {
}
