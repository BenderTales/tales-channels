package com.bendertales.mc.chatapi.api;

import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface PerRecipientPlaceholderFormatter {

	/**
	 *
	 * @param message
	 * @param recipient A player or null if console
	 * @return
	 */
	String format(Message message, ServerPlayerEntity recipient);

}
