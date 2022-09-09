package fr.bendertales.mc.channels.command.subcommands;

import java.util.Collections;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.bendertales.mc.channels.impl.ChatManager;
import net.minecraft.command.EntitySelector;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;


public class CmdChannelMute {

	private final SimpleCommandExceptionType exceptionType = new SimpleCommandExceptionType(Text.of("Channel not found"));
	private final ChatManager chatManager;

	public CmdChannelMute(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public int runAll(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var playerSelector = context.getArgument("player", EntitySelector.class);
		var player = playerSelector.getPlayer(cmdSource);

		var channels = chatManager.getChannels();
		chatManager.mutePlayerInChannels(player, channels);

		cmdSource.sendFeedback(getSuccessMessage(player), true);

		return channels.size();
	}

	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var playerSelector = context.getArgument("player", EntitySelector.class);
		var player = playerSelector.getPlayer(cmdSource);

		var channelId = context.getArgument("channel", Identifier.class);
		var optChannel = chatManager.getChannel(channelId);
		if (optChannel.isEmpty()) {
			throw exceptionType.create();
		}

		chatManager.mutePlayerInChannels(player, Collections.singleton(optChannel.get()));

		cmdSource.sendFeedback(getSuccessMessage(player), true);

		return SINGLE_SUCCESS;
	}

	private Text getSuccessMessage(ServerPlayerEntity player) {
		return Text.of(String.format("%s is now muted", player.getEntityName()));
	}
}
