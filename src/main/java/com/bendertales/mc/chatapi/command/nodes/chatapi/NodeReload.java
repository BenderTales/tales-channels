package com.bendertales.mc.chatapi.command.nodes.chatapi;

import java.util.List;

import com.bendertales.mc.chatapi.command.subcommands.CmdApiReload;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import com.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;


public class NodeReload implements TalesCommandNode {

	private final List<String>            permissions  = List.of("chatapi.commands.admin", "chatapi.commands.reload");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_FULL, permissions);

	private final CmdApiReload cmdApiReload;

	public NodeReload(ChatManager chatManager) {
		cmdApiReload = new CmdApiReload(chatManager);
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("reload")
		       .executes(cmdApiReload::run);
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
