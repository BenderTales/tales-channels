package com.bendertales.mc.chatapi.command.nodes.root;

import java.util.List;

import com.bendertales.mc.chatapi.command.shortcuts.CmdShortcut;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class NodeShortcut implements TalesCommandNode {

	private final String                  name;
	private final CommandNodeRequirements requirements;
	private final CmdShortcut             cmdShortcut;

	public NodeShortcut(String name, List<String> permissions,
	                    ChatManager chatManager, Identifier channelId) {
		this.name = name;
		this.requirements = CommandNodeRequirements.of(OP_SENIOR, permissions);
		this.cmdShortcut = new CmdShortcut(chatManager, channelId);
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal(name)
		       .requires(getRequirements().asPredicate())
		       .then(argument("message", StringArgumentType.greedyString())
	                .executes(cmdShortcut::run));
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
