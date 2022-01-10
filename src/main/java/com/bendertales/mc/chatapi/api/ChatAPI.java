package com.bendertales.mc.chatapi.api;

import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.Registry;


public class ChatAPI {

	public static MessageSender getMessageSender() {
		return ChatManager.get();
	}

	public void registerChannel(ChannelDefault channel) {
		Registry.CHANNEL_HANDLERS.register(channel);
	}

	public void registerPlaceholder(PlaceholderHandler placeholderHandler) {
		Registry.FORMAT_HANDLERS.register(placeholderHandler);
	}
}
