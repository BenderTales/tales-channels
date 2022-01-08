package com.bendertales.mc.chatapi.command;

import java.util.Collection;
import java.util.List;

import com.bendertales.mc.chatapi.api.ChatException;
import com.bendertales.mc.chatapi.command.suggestions.SenderChannelsSuggestionProvider;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CmdChat implements ModCommand {

	private final ChatManager chatManager;

	public CmdChat(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		dispatcher.register(
			literal("chat")
				.requires(getRequirements())
				.then(argument("channel", IdentifierArgumentType.identifier())
			        .suggests(new SenderChannelsSuggestionProvider(chatManager))
			        .then(argument("message", StringArgumentType.greedyString())
		                .executes(this)))
		);
	}

	@Override
	public Collection<String> getRequiredPermissions() {
		return List.of("chatapi.commands.admin", "chatapi.commands.chat");
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayer();

		var channelId = context.getArgument("channel", Identifier.class);
		var message = context.getArgument("message", String.class);

		try {
			chatManager.sendMessage(player, message, channelId);
			return SINGLE_SUCCESS;
		}
		catch (ChatException e) {
			var text = new LiteralText(e.getMessage()).formatted(Formatting.RED);
			throw new CommandSyntaxException(new SimpleCommandExceptionType(text), text);
		}
	}


}
