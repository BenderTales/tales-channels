package com.bendertales.mc.chatapi.impl.formats;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.MessageFormatter;
import com.bendertales.mc.chatapi.api.PlaceholderHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class PlayerNamePlaceholderHandler implements PlaceholderHandler {

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
	public MessageFormatter getMessageFormatter() {
		return (String format, ServerPlayerEntity player, String message) -> format.replace(PLAYER_NAME_PLACEHOLDER, player.getEntityName());
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Formats.PLAYER;
	}
}
