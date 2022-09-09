package fr.bendertales.mc.channels.command.nodes.channel;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.bendertales.mc.channels.command.subcommands.CmdChannelHideSet;
import fr.bendertales.mc.channels.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
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
