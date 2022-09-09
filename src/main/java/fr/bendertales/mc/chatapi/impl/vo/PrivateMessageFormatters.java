package fr.bendertales.mc.chatapi.impl.vo;

import fr.bendertales.mc.chatapi.impl.messages.MessageFormatter;


public record PrivateMessageFormatters(
		MessageFormatter consoleFormatter,
		MessageFormatter senderIsYouFormatter,
		MessageFormatter senderIsOtherFormatter
) {
}
