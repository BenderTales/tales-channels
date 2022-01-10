package com.bendertales.mc.chatapi.impl.vo;

import java.util.function.Predicate;

import com.bendertales.mc.chatapi.api.RecipientFilter;
import com.bendertales.mc.chatapi.impl.messages.MessageFormatter;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public record Channel(
		Identifier id,
		String selectorPrefix,
		MessageFormatter messageFormatter,
		RecipientFilter recipientsFilter,
		Predicate<ServerPlayerEntity> senderFilter
) {

}
