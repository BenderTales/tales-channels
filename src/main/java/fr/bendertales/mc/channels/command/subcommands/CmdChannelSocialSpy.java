package fr.bendertales.mc.channels.command.subcommands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.channels.impl.ChatManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


public class CmdChannelSocialSpy {

	private final ChatManager chatManager;

	public CmdChannelSocialSpy(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public int enableSocialSpy(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayerOrThrow();
		chatManager.enableSocialSpy(player);
		cmdSource.sendFeedback(Text.literal("Social spy enabled").formatted(Formatting.GREEN), true);
		return 0;
	}

	public int disableSocialSpy(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayerOrThrow();

		chatManager.disableSocialSpy(player);
		cmdSource.sendFeedback(Text.literal("Social spy disabled").formatted(Formatting.GOLD), true);
		return 0;
	}
}
