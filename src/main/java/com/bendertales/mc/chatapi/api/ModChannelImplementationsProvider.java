package com.bendertales.mc.chatapi.api;

import java.util.function.Predicate;

import net.minecraft.server.network.ServerPlayerEntity;


public interface ModChannelImplementationsProvider extends Identifiable {


	String getDefaultFormat();

	/**
	 * Define whether a player can send a message in this channel
	 * @return a boolean
	 */
	Predicate<ServerPlayerEntity> getSenderFilter();

	/**
	 * Get a function that filters the recipients of message in a channel.
	 * The function will be called for each connected player in the server who has not hidden the channel.
	 */
	RecipientFilter getRecipientsFilter();

	/**
	 * Define whether the channel is enabled before being configured
	 * @return a boolean
	 */
	default boolean isEnabledByDefault() {
		return true;
	}

	/**
	 * Defines a string that, if used as a prefix in chat, will set the message's channel into current one.
	 * @return the channel's prefix selector
	 */
	default String getPrefixSelector() {
		return null;
	}

}
