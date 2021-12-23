package com.bendertales.mc.chatapi.api;

import net.minecraft.server.network.ServerPlayerEntity;


public interface FormatHandler extends Identifiable {

	int getDefaultPriorityOrder();
	boolean shouldApplyFormat(String format);
	String handleMessage(String format, ServerPlayerEntity player, String message);
}
