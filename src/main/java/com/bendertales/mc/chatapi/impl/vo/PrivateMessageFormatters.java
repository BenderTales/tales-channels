package com.bendertales.mc.chatapi.impl.vo;

import com.bendertales.mc.chatapi.impl.messages.MessageFormatter;


public record PrivateMessageFormatters(
		MessageFormatter consoleFormatter,
		MessageFormatter senderIsYouFormatter,
		MessageFormatter senderIsOtherFormatter
) {
}
