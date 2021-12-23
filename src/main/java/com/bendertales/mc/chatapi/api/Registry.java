package com.bendertales.mc.chatapi.api;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Identifier;


public class Registry<T extends Identifiable> {

	public static final Registry<Channel>       CHANNEL_HANDLERS = new Registry<>();
	public static final Registry<FormatHandler> FORMAT_HANDLERS  = new Registry<>();

	private final Map<Identifier, T> handlers = new HashMap<>();

	private Registry() {
	}

	public void register(T object) {
		handlers.put(object.getId(), object);
	}
}
