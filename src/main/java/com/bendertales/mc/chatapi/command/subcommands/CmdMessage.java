package com.bendertales.mc.chatapi.command.subcommands;

import com.bendertales.mc.chatapi.api.ChatException;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.talesservercommon.commands.TalesCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.EntitySelector;
import net.minecraft.server.command.ServerCommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;


public class CmdMessage implements TalesCommand {

	private final ChatManager chatManager;

	public CmdMessage(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public int message(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var sender = cmdSource.getPlayerOrThrow();
		var playerSelector = context.getArgument("player", EntitySelector.class);
		var recipient = playerSelector.getPlayer(cmdSource);
		var message = context.getArgument("message", String.class);

		try {
			chatManager.sendPrivateMessage(sender, recipient, message);
		}
		catch (ChatException e) {
			throw asCommandException(e);
		}
		return SINGLE_SUCCESS;
	}
}
