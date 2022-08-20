package com.bendertales.mc.chatapi.api;


public interface PlaceholderHandler extends Identifiable {

	String getPlaceholder();
	boolean shouldApplyFormat(String format);

	PlaceholderFormatter getPlaceholderFormatter();

	default PerRecipientPlaceholderFormatter getPerRecipientPlaceholderFormatter() {
		return null;
	}

}
