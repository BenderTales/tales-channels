package com.bendertales.mc.chatapi.command.nodes.channel;

import com.bendertales.mc.chatapi.command.subcommands.CmdChannelHideSet;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import com.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;


public class NodeHideSet implements TalesCommandNode {

	private final CmdChannelHideSet cmdChannelHideSet;

	public NodeHideSet(ChatManager chatManager) {
		this.cmdChannelHideSet = new CmdChannelHideSet(chatManager);
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("set")
		       .then(literal("visible")
	                .executes(cmdChannelHideSet::setVisible))
		       .then(literal("invisible")
		             .executes(cmdChannelHideSet::setInvisible));
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return CommandNodeRequirements.noRequirements();
	}
}