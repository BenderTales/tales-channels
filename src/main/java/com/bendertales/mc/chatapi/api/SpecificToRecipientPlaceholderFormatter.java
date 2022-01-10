package com.bendertales.mc.chatapi.api;

import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface SpecificToRecipientPlaceholderFormatter {

	/**
	 *
	 * @param currentFormatState
	 * @param message
	 * @param recipient A player or null if console
	 * @return
	 */
	String formatMessage(String currentFormatState, Message message, ServerPlayerEntity recipient);

}
