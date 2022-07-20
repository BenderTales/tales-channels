package com.bendertales.mc.chatapi.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.bendertales.mc.chatapi.command.suggestions.SenderChannelsSuggestionProvider;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CmdUnmute implements ModCommand {

	private final ChatManager chatManager;

	public CmdUnmute(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess,
	                     CommandManager.RegistrationEnvironment environment) {
		dispatcher.register(
			literal("channel")
				.then(literal("unmute")
			        .requires(getRequirements())
				        .then(argument("player", EntityArgumentType.player())
				            .then(literal("*")
			                    .executes(this::runAll))
				            .then(argument("channel", IdentifierArgumentType.identifier())
		                        .suggests(new SenderChannelsSuggestionProvider(chatManager))
			                    .executes(this))))
		);
	}

	@Override
	public Collection<String> getRequiredPermissions() {
		return List.of("chatapi.commands.admin", "chatapi.commands.unmute");
	}

	public int runAll(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var targetPlayer = getTargetPlayer(context);

		var channels = chatManager.getChannels();

		chatManager.unmutePlayerInChannels(targetPlayer, channels);
		context.getSource().sendFeedback(getSuccessMessage(targetPlayer), true);
		return channels.size();
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var targetPlayer = getTargetPlayer(context);

		var channelId = context.getArgument("channel", Identifier.class);
		var channel = chatManager.getChannel(channelId);
		if (channel.isEmpty()) {
			var msg = Text.of("Channel not found");
			throw new CommandSyntaxException(new SimpleCommandExceptionType(msg), msg);
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
