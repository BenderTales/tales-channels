package fr.bendertales.mc.channels.command.nodes.root;

import java.util.List;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.bendertales.mc.channels.command.subcommands.CmdRespond;
import fr.bendertales.mc.channels.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class NodeRespond implements TalesCommandNode {

	private final List<String>            permissions  = List.of("chatapi.commands.admin", "chatapi.commands.private_message");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_JUNIOR, permissions);

	private final CmdRespond cmdRespond;

	public NodeRespond(ChatManager chatManager) {
		cmdRespond = new CmdRespond(chatManager);
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("r")
		       .requires(getRequirements().asPredicate())
		       .then(argument("message", StringArgumentType.greedyString())
	                .executes(cmdRespond::respond));
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
