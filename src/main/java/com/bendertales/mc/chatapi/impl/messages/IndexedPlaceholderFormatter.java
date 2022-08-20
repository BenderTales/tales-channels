package com.bendertales.mc.chatapi.impl.messages;

import com.bendertales.mc.chatapi.api.PlaceholderFormatter;


record IndexedPlaceholderFormatter(
		String key,
		PlaceholderFormatter formatter,
		int index,
		int length
) {
}
