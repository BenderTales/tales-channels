package com.bendertales.mc.chatapi.command;

import java.util.Collection;
import java.util.List;

import com.bendertales.mc.chatapi.api.ChatException;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CmdPrivateMessage implements ModCommand {

	private final ChatManager chatManager;

	public CmdPrivateMessage(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess,
	                     CommandManager.RegistrationEnvironment environment) {
		dispatcher.register(
			literal("msg")
				.requires(getRequirements())
				.then(argument("player", EntityArgumentType.player())
			        .then(argument("message", StringArgumentType.greedyString())
		                .executes(this)))
		);

		dispatcher.register(
			literal("r")
				.requires(getRequirements())
				.then(argument("message", StringArgumentType.greedyString())
			        .executes(this::respond))
		);
	}

	@Override
	public Collection<String> getRequiredPermissions() {
		return List.of("chatapi.commands.admin", "chatapi.commands.private_message");
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var sender = cmdSource.getPlayer();
		var playerSelector = context.getArgument("player", EntitySelector.class);
		var recipient = playerSelector.getPlayer(cmdSource);
		var message = context.getArgument("message", String.class);

		try {
			chatManager.sendPrivateMessage(sender, recipient, message);
		}
		catch (ChatException e) {
			var msg = Text.literal(e.getMessage()).formatted(Formatting.RED);
			throw new CommandSyntaxException(new SimpleCommandExceptionType(msg), msg);
		}
		return SINGLE_SUCCESS;
	}

	public int respond(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var sender = cmdSource.getPlayer();
		var message = context.getArgument("message", String.class);

		try {
			chatManager.respondToPrivateMessage(sender, message);
		}
		catch (ChatException e) {
			var msg = Text.literal(e.getMessage()).formatted(Formatting.RED);
			throw new CommandSyntaxException(new SimpleCommandExceptionType(msg), msg);
		}
		return SINGLE_SUCCESS;
	}


}
