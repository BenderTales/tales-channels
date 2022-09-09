package com.bendertales.mc.chatapi.command.subcommands;

import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.talesservercommon.commands.TalesCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.channels.api.ChatException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;


public class CmdChat implements TalesCommand {

	private final ChatManager chatManager;

	public CmdChat(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayerOrThrow();

		var channelId = context.getArgument("channel", Identifier.class);
		var message = context.getArgument("message", String.class);

		try {
			chatManager.sendMessage(player, message, channelId);
			return SINGLE_SUCCESS;
		}
		catch (ChatException e) {
			throw asCommandException(e);
		}
	}
}
