package fr.bendertales.mc.chatapi.command.nodes.channel;

import java.util.List;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.bendertales.mc.chatapi.command.subcommands.CmdChannelList;
import fr.bendertales.mc.chatapi.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;


public class NodeList implements TalesCommandNode {

	private final List<String>            permissions  = List.of("chatapi.commands.admin", "chatapi.commands.list");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_JUNIOR, permissions);

	private final CmdChannelList cmdChannelList;

	public NodeList(ChatManager chatManager) {
		this.cmdChannelList = new CmdChannelList(chatManager);
	}

	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("list")
			.requires(getRequirements().asPredicate())
			.executes(cmdChannelList::run);
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
