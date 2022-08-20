package com.bendertales.mc.chatapi.api;


@FunctionalInterface
public interface PlaceholderFormatter {

	String format(Message message);
}
