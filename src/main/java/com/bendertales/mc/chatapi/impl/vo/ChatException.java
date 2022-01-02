package com.bendertales.mc.chatapi.impl.vo;

public class ChatException extends Exception {

	public ChatException(String message) {
		super(message);
	}

	public ChatException(String message, Throwable cause) {
		super(message, cause);
	}
}
