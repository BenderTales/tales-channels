package com.bendertales.mc.chatapi.api;


public abstract class AbstractPlaceholderHandler implements PlaceholderHandler {

	private final String placeholder;

	public AbstractPlaceholderHandler(String placeholder) {
		this.placeholder = placeholder;
	}

	@Override
	public String getPlaceholder() {
		return placeholder;
	}

	@Override
	public boolean shouldApplyFormat(String format) {
		return format.contains(placeholder);
	}
}
