package com.bendertales.mc.chatapi.command.subcommands;

import com.bendertales.mc.chatapi.api.ChatException;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.talesservercommon.commands.TalesCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;


public class CmdRespond implements TalesCommand {

	private final ChatManager chatManager;

	public CmdRespond(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public int respond(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var sender = cmdSource.getPlayerOrThrow();
		var message = context.getArgument("message", String.class);

		try {
			chatManager.respondToPrivateMessage(sender, message);
		}
		catch (ChatException e) {
			throw asCommandException(e);
		}
		return SINGLE_SUCCESS;
	}
}
