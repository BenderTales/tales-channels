package com.bendertales.mc.chatapi.api;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public interface MessageSender {

	void sendMessage(ServerPlayerEntity sender, String message, Identifier channelId) throws ChatException;
}
