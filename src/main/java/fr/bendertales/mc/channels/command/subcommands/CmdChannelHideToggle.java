package fr.bendertales.mc.channels.command.subcommands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.bendertales.mc.channels.impl.ChatManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class CmdChannelHideToggle {

	private final SimpleCommandExceptionType notFoundException
			= new SimpleCommandExceptionType(Text.of("Channel not found"));

	private final ChatManager chatManager;

	public CmdChannelHideToggle(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayerOrThrow();
		var channelId = context.getArgument("channel", Identifier.class);

		var optChannel = chatManager.getChannel(channelId);
		if (optChannel.isEmpty()) {
			throw notFoundException.create();
		}

		boolean hidden = chatManager.toggleHiddenChannelForPlayer(optChannel.get(), player);
		var msg = Text.of(hidden ? "Channel successfully hidden" : "Channel now visible");
		player.sendMessage(msg, false);

		return SINGLE_SUCCESS;
	}
}
