package com.bendertales.mc.chatapi.config.serialization;

import java.lang.reflect.Type;

import com.google.gson.*;
import net.minecraft.util.Identifier;


public class IdentifierSerializer implements JsonSerializer<Identifier>, JsonDeserializer<Identifier> {
	@Override
	public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	throws JsonParseException {
		return new Identifier(json.getAsString());
	}

	@Override
	public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.toString());
	}
}
