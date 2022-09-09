package com.bendertales.mc.chatapi.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

import fr.bendertales.mc.channels.api.Identifiable;
import fr.bendertales.mc.channels.api.ModChannelImplementationsProvider;
import fr.bendertales.mc.channels.api.PlaceholderHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;


public final class Registry<T extends Identifiable> implements Iterable<T> {

	public static final Registry<ModChannelImplementationsProvider> CHANNEL_HANDLERS = new Registry<>();
	public static final Registry<PlaceholderHandler>                FORMAT_HANDLERS  = new Registry<>();

	private final Map<Identifier, T> handlers = new HashMap<>();

	private Registry() {
	}

	public void register(T object) {
		handlers.put(object.getId(), object);
	}

	public T get(Identifier key) {
		return handlers.get(key);
	}

	public Stream<T> stream() {
		return handlers.values().stream();
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return handlers.values().iterator();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		handlers.values().forEach(action);
	}

	@Override
	public Spliterator<T> spliterator() {
		return handlers.values().spliterator();
	}
}
