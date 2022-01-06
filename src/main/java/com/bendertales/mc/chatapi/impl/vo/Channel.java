package com.bendertales.mc.chatapi.impl.vo;

import java.util.Collection;
import java.util.function.Predicate;

import com.bendertales.mc.chatapi.api.RecipientFilter;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public record Channel(
		Identifier id,
		String selectorPrefix,
		String format,
		Collection<Placeholder> placeholderHandlers,
		RecipientFilter recipientsFilter,
		Predicate<ServerPlayerEntity> senderFilter
) {

	public String formatMessage(ServerPlayerEntity sender, String message) {
		String toSend = format;

		for (Placeholder placeholder : placeholderHandlers) {
			toSend = placeholder.messageFormatter().formatMessage(toSend, sender, message);
		}

		return toSend;
	}
}
