package com.bendertales.mc.chatapi.api;

import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.Registry;


public class ChatAPI {

	public static Messenger getMessageSender() {
		return ChatManager.get();
	}

	public static void registerChannel(ModChannelImplementationsProvider channelProvider) {
		Registry.CHANNEL_HANDLERS.register(channelProvider);
	}

	public static void registerPlaceholder(PlaceholderHandler placeholderHandler) {
		Registry.FORMAT_HANDLERS.register(placeholderHandler);
	}
}
