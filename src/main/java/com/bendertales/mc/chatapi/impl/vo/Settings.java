package com.bendertales.mc.chatapi.impl.vo;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.util.Identifier;


public record Settings(
	Identifier defaultChannel,
	int localChannelDistance,
	PrivateMessageFormatters privateMessageFormatters,
	Object2ObjectMap<Identifier, Channel> channels
) {
}
