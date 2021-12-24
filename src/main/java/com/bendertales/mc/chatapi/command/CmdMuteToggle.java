package com.bendertales.mc.chatapi.command;

import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;


public class CmdMuteToggle implements Command<ServerCommandSource> {

	private final ChatManager chatManager;

	public CmdMuteToggle(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return 0;
	}
}
