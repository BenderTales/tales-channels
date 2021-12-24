package com.bendertales.mc.chatapi.api;

import net.minecraft.server.network.ServerPlayerEntity;


public interface MessageFormatter {

	String formatMessage(String currentFormatState, ServerPlayerEntity player, String message);
}
