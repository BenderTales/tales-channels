package com.bendertales.mc.chatapi.client;

import com.bendertales.mc.chatapi.client.command.ChatConfigCommand;
import com.bendertales.mc.chatapi.client.gui.ChatScreen;
import com.bendertales.mc.chatapi.client.gui.ChatSettingsGui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;


@Environment(EnvType.CLIENT)
public class ChatApiClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		var chatConfigCommand = new ChatConfigCommand();

		ClientCommandManager.DISPATCHER.register(chatConfigCommand.asCommandBuilder());

		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof OptionsScreen) {
				var buttonWidget = new ButtonWidget(screen.width / 2 + 5, screen.height / 6 + 144 - 6, 150, 20, Text.of("Channels"), (btn) -> {
					MinecraftClient.getInstance().setScreen(new ChatScreen(new ChatSettingsGui()));
				});
				Screens.getButtons(screen).add(buttonWidget);
			}
		});
	}
}
