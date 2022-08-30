package com.bendertales.mc.chatapi.command.nodes.channel;

import java.util.List;

import com.bendertales.mc.chatapi.command.subcommands.CmdChannelUnmute;
import com.bendertales.mc.chatapi.command.suggestions.SenderChannelsSuggestionProvider;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import com.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class NodeUnmute implements TalesCommandNode {

	private final List<String>            permissions  = List.of("chatapi.commands.admin", "chatapi.commands.unmute");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_MEDIOR, permissions);

	private final CmdChannelUnmute                 cmdChannelUnmute;
	private final SenderChannelsSuggestionProvider suggestionProvider;

	public NodeUnmute (ChatManager chatManager) {
		this.cmdChannelUnmute = new CmdChannelUnmute(chatManager);
		suggestionProvider = new SenderChannelsSuggestionProvider(chatManager);
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("unmute")
		       .requires(getRequirements().asPredicate())
		       .then(argument("player", EntityArgumentType.player())
	                .then(literal("*")
                        .executes(cmdChannelUnmute::runAll))
	                .then(argument("channel", IdentifierArgumentType.identifier())
	                    .suggests(suggestionProvider)
	                    .executes(cmdChannelUnmute::run)));
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
