package com.bendertales.mc.chatapi.command.shortcuts;

import com.bendertales.mc.talesservercommon.commands.TalesCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.channels.api.ChatException;
import fr.bendertales.mc.channels.api.Messenger;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;


public class CmdShortcut implements TalesCommand {

	private final Messenger  messenger;
	private final Identifier channelId;

	public CmdShortcut(Messenger messenger, Identifier channelId) {
		this.messenger = messenger;
		this.channelId = channelId;
	}

	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayer();
		var message = context.getArgument("message", String.class);

		try {
			messenger.sendMessage(player, message, channelId);
		}
		catch (ChatException e) {
			throw asCommandException(e);
		}
		return SINGLE_SUCCESS;
	}
}
