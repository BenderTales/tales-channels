package fr.bendertales.mc.channels.command.subcommands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.channels.impl.ChatManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;


public class CmdApiReload {

	private final ChatManager chatManager;

	public CmdApiReload(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		chatManager.reload();
		context.getSource().sendFeedback(Text.of("Chat-Api reloaded"), true);
		return 0;
	}
}
