package com.bendertales.mc.chatapi;

import com.bendertales.mc.chatapi.api.Registry;
import com.bendertales.mc.chatapi.command.CommandRegistries;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.channels.*;
import com.bendertales.mc.chatapi.impl.formats.DateTimePlaceholderHandler;
import com.bendertales.mc.chatapi.impl.formats.MessagePlaceholderHandler;
import com.bendertales.mc.chatapi.impl.formats.PlayerNamePlaceholderHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ChatApi implements ModInitializer {

	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {

		var chatManager = ChatManager.get();

		CommandRegistries.registerCommands(chatManager);

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			LOGGER.info("Registering format and channel handlers");
			chatManager.setMinecraftServer(server);

			Registry.FORMAT_HANDLERS.register(new MessagePlaceholderHandler());
			Registry.FORMAT_HANDLERS.register(new PlayerNamePlaceholderHandler());
			Registry.FORMAT_HANDLERS.register(new DateTimePlaceholderHandler());

			Registry.CHANNEL_HANDLERS.register(new LocalChannel());
			Registry.CHANNEL_HANDLERS.register(new GlobalChannel());
			Registry.CHANNEL_HANDLERS.register(new SupportChannel());
			Registry.CHANNEL_HANDLERS.register(new StaffChannel());
			Registry.CHANNEL_HANDLERS.register(new HelpersChannel());
			Registry.CHANNEL_HANDLERS.register(new ModerationChannel());
			Registry.CHANNEL_HANDLERS.register(new AdminChannel());
		});

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			LOGGER.info("Loading chat configuration");
			chatManager.load();
		});

	}

}
