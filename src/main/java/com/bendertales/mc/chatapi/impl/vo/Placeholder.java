package com.bendertales.mc.chatapi.impl.vo;

import com.bendertales.mc.chatapi.api.PlaceholderFormatter;
import com.bendertales.mc.chatapi.api.SpecificToRecipientPlaceholderFormatter;
import net.minecraft.util.Identifier;


public record Placeholder(
		Identifier id,
		int applyOrder,
		PlaceholderFormatter placeholderFormatter,
		SpecificToRecipientPlaceholderFormatter recipientPlaceholderFormatter
) {
}
