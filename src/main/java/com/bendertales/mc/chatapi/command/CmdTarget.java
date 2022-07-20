package com.bendertales.mc.chatapi.command;

import java.util.Collection;
import java.util.List;

import com.bendertales.mc.chatapi.api.ChatException;
import com.bendertales.mc.chatapi.command.suggestions.SenderChannelsSuggestionProvider;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CmdTarget implements ModCommand {

	private final ChatManager chatManager;

	public CmdTarget(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayerOrThrow();
		var channelId = context.getArgument("channel", Identifier.class);

		try {
			chatManager.changeTargetedChannel(player, channelId);
			var msg = Text.literal("%s is now the active channel".formatted(channelId)).formatted(Formatting.GREEN);
			player.sendMessage(msg, MessageType.SYSTEM);
		}
		catch (ChatException e) {
			var message = Text.of(e.getMessage());
			var type = new SimpleCommandExceptionType(message);
			throw new CommandSyntaxException(type, message);
		}
		return 0;
	}

	@Override
	public Collection<String> getRequiredPermissions() {
		return List.of("chatapi.commands.admin", "chatapi.commands.target");
	}

	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess,
	                     CommandManager.RegistrationEnvironment environment) {
		dispatcher.register(
			literal("channel")
				.then(literal("target")
			        .requires(getRequirements())
				        .then(argument("channel", IdentifierArgumentType.identifier())
			                .suggests(new SenderChannelsSuggestionProvider(chatManager))
			                .executes(this)))
		);
	}
}
