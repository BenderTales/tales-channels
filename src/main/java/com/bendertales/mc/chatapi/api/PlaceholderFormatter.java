package com.bendertales.mc.chatapi.api;


@FunctionalInterface
public interface PlaceholderFormatter {

	String formatMessage(String currentLineState, Message message);
}
