package fr.bendertales.mc.channels;

import net.minecraft.util.Identifier;


public final class ModConstants {

	public static final String MODID = "tales-channels";

	public static final class Ids {

		public static final class Formats {
			public static final Identifier MESSAGE   = new Identifier("chat", "message");
			public static final Identifier SENDER    = new Identifier("chat", "sender");
			public static final Identifier RECIPIENT = new Identifier("chat", "recipient");
			public static final Identifier TIME      = new Identifier("chat", "time");
		}

		public static final class Channels {
			public static final Identifier LOCAL   = new Identifier("chat", "local");
			public static final Identifier GLOBAL  = new Identifier("chat", "global");
			public static final Identifier SUPPORT = new Identifier("chat", "support");
			public static final Identifier STAFF   = new Identifier("chat", "staff");
			public static final Identifier ADMIN   = new Identifier("chat", "administration");
			public static final Identifier MODO    = new Identifier("chat", "moderation");
			public static final Identifier HELPERS = new Identifier("chat", "helpers");
		}
	}
}
