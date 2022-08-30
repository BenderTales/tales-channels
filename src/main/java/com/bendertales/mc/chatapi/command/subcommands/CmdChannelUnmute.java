package com.bendertales.mc.chatapi.command.subcommands;

import java.util.Collections;

import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.EntitySelector;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class CmdChannelUnmute {

	private final SimpleCommandExceptionType exceptionType = new SimpleCommandExceptionType(Text.of("Channel not found"));
	private final ChatManager                chatManager;

	public CmdChannelUnmute(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public int runAll(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var targetPlayer = getTargetPlayer(context);

		var channels = chatManager.getChannels();

		chatManager.unmutePlayerInChannels(targetPlayer, channels);
		context.getSource().sendFeedback(getSuccessMessage(targetPlayer), true);
		return channels.size();
	}

	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var targetPlayer = getTargetPlayer(context);

		var channelId = context.getArgument("channel", Identifier.class);
		var channel = chatManager.getChannel(channelId);
		if (channel.isEmpty()) {
			throw exceptionType.create();
		}

		chatManager.unmutePlayerInChannels(targetPlayer, Collections.singleton(channel.get()));
		context.getSource().sendFeedback(getSuccessMessage(targetPlayer), true);
		return 0;
	}

	private ServerPlayerEntity getTargetPlayer(CommandContext<ServerCommandSource> context)
	throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var playerSelector = context.getArgument("player", EntitySelector.class);
		return playerSelector.getPlayer(cmdSource);
	}

	private Text getSuccessMessage(ServerPlayerEntity player) {
		return Text.of(String.format("%s is now allowed to speak", player.getEntityName()));
	}
}
