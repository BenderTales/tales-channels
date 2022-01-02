package com.bendertales.mc.chatapi.client.command;

import com.bendertales.mc.chatapi.client.gui.ChatScreen;
import com.bendertales.mc.chatapi.client.gui.ChatSettingsGui;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;


public class ChatConfigCommand implements Command<FabricClientCommandSource> {

	public LiteralArgumentBuilder<FabricClientCommandSource> asCommandBuilder() {
		return literal("chatconfig").executes(this);
	}


	@Override
	public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
		MinecraftClient.getInstance().setScreen(new ChatScreen(new ChatSettingsGui()));
		return 0;
	}
}
