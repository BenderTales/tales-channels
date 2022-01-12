package com.bendertales.mc.chatapi.api;

import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.Registry;


public class ChatAPI {

	public static MessageSender getMessageSender() {
		return ChatManager.get();
	}

	public static void registerChannel(ChannelDefault channel) {
		Registry.CHANNEL_HANDLERS.register(channel);
	}

	public static void registerPlaceholder(PlaceholderHandler placeholderHandler) {
		Registry.FORMAT_HANDLERS.register(placeholderHandler);
	}
}
