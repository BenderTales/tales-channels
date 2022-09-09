package fr.bendertales.mc.channels.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


@Environment(EnvType.CLIENT)
public class ChatApiClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		/*
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof OptionsScreen) {
				var buttonWidget = new ButtonWidget(screen.width / 2 + 5, screen.height / 6 + 144 - 6, 150, 20, Text.of("Channels"), (btn) -> {
					MinecraftClient.getInstance().setScreen(new ChatScreen(new ChatSettingsGui()));
				});
				Screens.getButtons(screen).add(buttonWidget);
			}
		});*/
	}
}
