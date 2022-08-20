package com.bendertales.mc.chatapi.impl.vo;

import com.bendertales.mc.chatapi.api.PerRecipientPlaceholderFormatter;
import com.bendertales.mc.chatapi.api.PlaceholderFormatter;
import net.minecraft.util.Identifier;


public record Placeholder(
		Identifier id,
		String key,
		PlaceholderFormatter placeholderFormatter,
		PerRecipientPlaceholderFormatter recipientPlaceholderFormatter
) {
}
