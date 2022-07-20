package com.bendertales.mc.chatapi.command;

import java.util.Collection;
import java.util.List;

import com.bendertales.mc.chatapi.command.suggestions.SenderChannelsSuggestionProvider;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CmdHideToggle implements ModCommand {

	private final ChatManager chatManager;

	public CmdHideToggle(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess,
	                     CommandManager.RegistrationEnvironment environment) {
		dispatcher.register(
			literal("channel")
				.then(literal("hide")
			        .then(literal("toggle")
		                .requires(getRequirements())
		                .then(argument("channel", IdentifierArgumentType.identifier())
	                        .suggests(new SenderChannelsSuggestionProvider(chatManager))
	                        .executes(this))))
		);
	}

	@Override
	public Collection<String> getRequiredPermissions() {
		return List.of("chatapi.commands.admin", "chatapi.commands.hide.toggle");
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayer();
		var channelId = context.getArgument("channel", Identifier.class);

		var optChannel = chatManager.getChannel(channelId);
		if (optChannel.isEmpty()) {
			var msg = Text.of("Channel not found");
			throw new CommandSyntaxException(new SimpleCommandExceptionType(msg), msg);
		}

		boolean hidden = chatManager.toggleHiddenChannelForPlayer(optChannel.get(), player);
		var msg = Text.of(hidden ? "Channel successfully hidden" : "Channel now visible");
		player.sendMessage(msg, false);

		return SINGLE_SUCCESS;
	}
}
