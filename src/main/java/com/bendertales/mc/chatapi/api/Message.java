package com.bendertales.mc.chatapi.api;

import net.minecraft.server.network.ServerPlayerEntity;


public record Message(
		ServerPlayerEntity sender,
		String content
) {
}
