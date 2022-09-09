package fr.bendertales.mc.channels.impl.vo;

import java.util.function.Predicate;

import fr.bendertales.mc.channels.api.RecipientFilter;
import fr.bendertales.mc.channels.impl.messages.MessageFormatter;
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
