package com.bendertales.mc.chatapi.api;

import java.util.function.Predicate;

import net.minecraft.server.network.ServerPlayerEntity;


public interface ChannelDefault extends Identifiable {


	String getDefaultFormat();
	Predicate<ServerPlayerEntity> getSenderFilter();
	Predicate<ServerPlayerEntity> getRecipientsFilter();

	default boolean isEnabledByDefault() {
		return true;
	}

	default String getPrefixSelector() {
		return null;
	}

}
