package com.bendertales.mc.chatapi;

import net.minecraft.util.Identifier;


public final class ChatConstants {

	public static final String MODID = "chat-api";

	public static final class Ids {

		public static final class Formats {
			public static final Identifier MESSAGE = new Identifier("chat", "message");
			public static final Identifier PLAYER = new Identifier("chat", "player");
			public static final Identifier TIME = new Identifier("chat", "time");
		}

		public static final class Channels {
			public static final Identifier LOCAL = new Identifier("chat", "local");
			public static final Identifier GLOBAL = new Identifier("chat", "global");
			public static final Identifier SUPPORT = new Identifier("chat", "support");
			public static final Identifier STAFF = new Identifier("chat", "staff");
			public static final Identifier ADMIN = new Identifier("chat", "administration");
			public static final Identifier MODO = new Identifier("chat", "moderation");
			public static final Identifier HELPERS = new Identifier("chat", "helpers");
		}

	}

	private ChatConstants() {
	}
}
