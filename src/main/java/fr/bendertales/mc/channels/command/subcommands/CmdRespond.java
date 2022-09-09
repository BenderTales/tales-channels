package fr.bendertales.mc.channels.command.subcommands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.channels.api.ChatException;
import fr.bendertales.mc.channels.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.TalesCommand;
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
