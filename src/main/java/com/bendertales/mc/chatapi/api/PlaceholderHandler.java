package com.bendertales.mc.chatapi.api;


public interface PlaceholderHandler extends Identifiable {

	int getDefaultPriorityOrder();
	boolean shouldApplyFormat(String format);
	MessageFormatter getMessageFormatter();

	default boolean isEnabledByDefault() {
		return true;
	}
}
