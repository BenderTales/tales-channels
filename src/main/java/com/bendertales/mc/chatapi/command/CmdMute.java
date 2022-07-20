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


public class CmdMute implements ModCommand {

	private final ChatManager chatManager;

	public CmdMute(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess,
	                     CommandManager.RegistrationEnvironment environment) {
		dispatcher.register(
			literal("channel")
				.then(literal("mute")
			        .requires(getRequirements())
			        .then(argument("player", EntityArgumentType.player())
		                .then(literal("*")
	                        .executes(this::runAll))
		                .then(argument("channel", IdentifierArgumentType.identifier())
	                        .suggests(new SenderChannelsSuggestionProvider(chatManager))
	                        .executes(this))
			        )
				)
		);
	}

	@Override
	public Collection<String> getRequiredPermissions() {
		return List.of("chatapi.commands.admin","chatapi.commands.mute");
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

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var playerSelector = context.getArgument("player", EntitySelector.class);
		var player = playerSelector.getPlayer(cmdSource);

		var channelId = context.getArgument("channel", Identifier.class);
		var optChannel = chatManager.getChannel(channelId);
		if (optChannel.isEmpty()) {
			var msg = Text.of("Channel not found");
			throw new CommandSyntaxException(new SimpleCommandExceptionType(msg), msg);
		}

		chatManager.mutePlayerInChannels(player, Collections.singleton(optChannel.get()));

		cmdSource.sendFeedback(getSuccessMessage(player), true);

		return SINGLE_SUCCESS;
	}

	private Text getSuccessMessage(ServerPlayerEntity player) {
		return Text.of(String.format("%s is now muted", player.getEntityName()));
	}


}
