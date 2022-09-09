package fr.bendertales.mc.channels.command.subcommands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.channels.api.ChatException;
import fr.bendertales.mc.channels.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.TalesCommand;
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
