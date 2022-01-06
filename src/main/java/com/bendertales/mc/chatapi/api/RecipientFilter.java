package com.bendertales.mc.chatapi.api;

import net.minecraft.server.network.ServerPlayerEntity;


@FunctionalInterface
public interface RecipientFilter {

	boolean filterRecipient(ServerPlayerEntity sender, ServerPlayerEntity recipient, RecipientFilterOptions options);
}
