package com.bendertales.mc.chatapi.impl.vo;

import java.util.Collection;
import java.util.function.Predicate;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public record Channel(
		Identifier id,
		String format,
		Collection<Placeholder> placeholderHandlers,
		Predicate<ServerPlayerEntity> recipientsFilter,
		Predicate<ServerPlayerEntity> senderFilter
) {
}
