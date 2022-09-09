package com.bendertales.mc.chatapi.command.nodes.channel;

import com.bendertales.mc.chatapi.command.subcommands.CmdChannelHideToggle;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;


public class NodeHideToggle implements TalesCommandNode {

	private final CmdChannelHideToggle       hideToggle;

	public NodeHideToggle(ChatManager chatManager) {
		this.hideToggle = new CmdChannelHideToggle(chatManager);
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("toggle")
            .executes(hideToggle::run);
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return CommandNodeRequirements.noRequirements();
	}

}
