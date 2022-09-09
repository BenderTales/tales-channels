package com.bendertales.mc.chatapi.command.nodes.root;

import java.util.List;

import com.bendertales.mc.chatapi.command.subcommands.CmdChat;
import com.bendertales.mc.chatapi.command.suggestions.SenderChannelsSuggestionProvider;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class NodeChat implements TalesCommandNode {

	private final List<String>            permissions  = List.of("chatapi.commands.admin", "chatapi.commands.chat");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_JUNIOR, permissions);

	private final CmdChat                          cmdChat;
	private final SenderChannelsSuggestionProvider suggestionProvider;

	public NodeChat(ChatManager chatManager) {
		cmdChat = new CmdChat(chatManager);
		suggestionProvider = new SenderChannelsSuggestionProvider(chatManager);
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("chat")
	        .requires(getRequirements().asPredicate())
	        .then(argument("channel", IdentifierArgumentType.identifier())
                .suggests(suggestionProvider)
                .then(argument("message", StringArgumentType.greedyString())
                    .executes(cmdChat::run)));
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
