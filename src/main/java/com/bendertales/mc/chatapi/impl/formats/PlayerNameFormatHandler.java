package com.bendertales.mc.chatapi.impl.formats;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.FormatHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class PlayerNameFormatHandler implements FormatHandler {

	public static final String PLAYER_NAME_PLACEHOLDER = "%PLAYER_NAME%";

	@Override
	public int getDefaultPriorityOrder() {
		return -1;
	}

	@Override
	public boolean shouldApplyFormat(String format) {
		return format.contains(PLAYER_NAME_PLACEHOLDER);
	}

	@Override
	public String handleMessage(String format, ServerPlayerEntity player, String message) {
		return format.replace(PLAYER_NAME_PLACEHOLDER, player.getEntityName());
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Formats.PLAYER;
	}
}
