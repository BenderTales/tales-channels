package com.bendertales.mc.chatapi.api;

import java.util.function.Predicate;

import net.minecraft.server.network.ServerPlayerEntity;


public interface Channel extends Identifiable{

	String getDefaultFormat();
	Predicate<ServerPlayerEntity> getChannelUsabilityFilter();
	Predicate<ServerPlayerEntity> getChannelRecipientFilter();

}
