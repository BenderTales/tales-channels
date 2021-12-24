package com.bendertales.mc.chatapi.impl.vo;

import com.bendertales.mc.chatapi.api.MessageFormatter;
import net.minecraft.util.Identifier;


public record Placeholder(
		Identifier id,
		int applyOrder,
		MessageFormatter messageFormatter
) {
}
