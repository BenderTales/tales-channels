package com.bendertales.mc.chatapi.api;

import net.minecraft.server.network.ServerPlayerEntity;


@FunctionalInterface
public interface RecipientFilter {

	MessageVisibility filterRecipient(ServerPlayerEntity sender, ServerPlayerEntity recipient, RecipientFilterOptions options);
}
