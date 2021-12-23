package com.bendertales.mc.chatapi;

import com.bendertales.mc.chatapi.api.Registry;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.channels.*;
import com.bendertales.mc.chatapi.impl.formats.DateTimeFormatHandler;
import com.bendertales.mc.chatapi.impl.formats.MessageFormatHandler;
import com.bendertales.mc.chatapi.impl.formats.PlayerNameFormatHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ChatApi implements ModInitializer {

	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {

		var chatManager = new ChatManager();

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			LOGGER.info("Registering format and channel handlers");
			Registry.FORMAT_HANDLERS.register(new MessageFormatHandler());
			Registry.FORMAT_HANDLERS.register(new PlayerNameFormatHandler());
			Registry.FORMAT_HANDLERS.register(new DateTimeFormatHandler());

			Registry.CHANNEL_HANDLERS.register(new LocalChannel(chatManager));
			Registry.CHANNEL_HANDLERS.register(new GlobalChannel(chatManager));
			Registry.CHANNEL_HANDLERS.register(new StaffChannel(chatManager));
			Registry.CHANNEL_HANDLERS.register(new HelpersChannel(chatManager));
			Registry.CHANNEL_HANDLERS.register(new ModerationChannel(chatManager));
			Registry.CHANNEL_HANDLERS.register(new AdminChannel(chatManager));
		});

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			LOGGER.info("Loading chat configuration");

		});

	}

}
