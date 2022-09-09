package fr.bendertales.mc.chatapi.command.subcommands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.channels.api.ChatException;
import fr.bendertales.mc.chatapi.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.TalesCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;


public class CmdChannelSelect implements TalesCommand {

	private final ChatManager chatManager;

	public CmdChannelSelect(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayerOrThrow();
		var channelId = context.getArgument("channel", Identifier.class);

		try {
			chatManager.changeTargetedChannel(player, channelId);
			var msg = Text.literal("%s is now the active channel".formatted(channelId)).formatted(Formatting.GREEN);
			player.sendMessage(msg, true);
		}
		catch (ChatException e) {
			throw asCommandException(e);
		}
		return 0;
	}
}
