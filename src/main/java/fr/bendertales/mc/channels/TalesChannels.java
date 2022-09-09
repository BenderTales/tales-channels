package fr.bendertales.mc.channels;

import fr.bendertales.mc.channels.command.CommandRegistries;
import fr.bendertales.mc.channels.impl.ChatManager;
import fr.bendertales.mc.channels.impl.Registry;
import fr.bendertales.mc.channels.impl.channels.*;
import fr.bendertales.mc.channels.impl.formats.DateTimePlaceholderHandler;
import fr.bendertales.mc.channels.impl.formats.MessagePlaceholderHandler;
import fr.bendertales.mc.channels.impl.formats.RecipientPlaceholderHandler;
import fr.bendertales.mc.channels.impl.formats.SenderPlaceholderHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TalesChannels implements ModInitializer {

	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {

		var chatManager = ChatManager.get();

		CommandRegistries.registerCommands(chatManager);

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			LOGGER.info("Registering format and channel handlers");
			chatManager.setMinecraftServer(server);

			Registry.FORMAT_HANDLERS.register(new MessagePlaceholderHandler());
			Registry.FORMAT_HANDLERS.register(new SenderPlaceholderHandler());
			Registry.FORMAT_HANDLERS.register(new RecipientPlaceholderHandler());
			Registry.FORMAT_HANDLERS.register(new DateTimePlaceholderHandler());

			Registry.CHANNEL_HANDLERS.register(new LocalChannel(chatManager));
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
