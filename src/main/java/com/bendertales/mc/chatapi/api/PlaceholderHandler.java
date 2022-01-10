package com.bendertales.mc.chatapi.api;


public interface PlaceholderHandler extends Identifiable {

	int getDefaultPriorityOrder();
	boolean shouldApplyFormat(String format);

	PlaceholderFormatter getPlaceholderFormatter();

	default SpecificToRecipientPlaceholderFormatter getSpecificToRecipientPlaceholderFormatter() {
		return null;
	}

}
