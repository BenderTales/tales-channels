package fr.bendertales.mc.channels.command.nodes.channel;

import java.util.List;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.bendertales.mc.channels.command.subcommands.CmdChannelSelect;
import fr.bendertales.mc.channels.command.suggestions.SenderChannelsSuggestionProvider;
import fr.bendertales.mc.channels.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class NodeSelect implements TalesCommandNode {

	private final List<String>            permissions  = List.of("chatapi.commands.admin", "chatapi.commands.selected");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_JUNIOR, permissions);

	private final CmdChannelSelect                 cmdChannelSelect;
	private final SenderChannelsSuggestionProvider suggestionProvider;

	public NodeSelect(ChatManager chatManager) {
		cmdChannelSelect = new CmdChannelSelect(chatManager);
		suggestionProvider = new SenderChannelsSuggestionProvider(chatManager);
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("select")
		       .requires(getRequirements().asPredicate())
		       .then(argument("channel", IdentifierArgumentType.identifier())
	                .suggests(suggestionProvider)
	                .executes(cmdChannelSelect::run));
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
