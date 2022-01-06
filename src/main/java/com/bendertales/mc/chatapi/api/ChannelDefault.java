package com.bendertales.mc.chatapi.api;

import java.util.function.Predicate;

import net.minecraft.server.network.ServerPlayerEntity;


public interface ChannelDefault extends Identifiable {


	String getDefaultFormat();
	Predicate<ServerPlayerEntity> getSenderFilter();

	/**
	 * Get a function that filters the recipients of message in a channel.
	 * The function will be called for each connected player in the server who has not hidden the channel.
	 * @return
	 */
	RecipientFilter getRecipientsFilter();

	default boolean isEnabledByDefault() {
		return true;
	}

	default String getPrefixSelector() {
		return null;
	}

}
