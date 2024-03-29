package fr.bendertales.mc.channels.command.nodes.channel;

import java.util.List;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.bendertales.mc.channels.command.suggestions.SenderChannelsSuggestionProvider;
import fr.bendertales.mc.channels.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.AbstractIntermediaryCommandNode;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class NodeHide extends AbstractIntermediaryCommandNode {

	private final List<String>            permissions  = List.of("chatapi.commands.admin", "chatapi.commands.hide");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_JUNIOR, permissions);

	private final SenderChannelsSuggestionProvider suggestionProvider;

	public NodeHide(ChatManager chatManager) {
		super(List.of(
			new NodeHideToggle(chatManager),
			new NodeHideSet(chatManager)
		));

		this.suggestionProvider = new SenderChannelsSuggestionProvider(chatManager);
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		var hideNode = literal(getName())
				               .requires(getRequirements().asPredicate());
		var channelNode = argument("channel", IdentifierArgumentType.identifier())
				                  .suggests(suggestionProvider);

		hideNode.then(channelNode);

		getChildrenNodes().forEach(channelNode::then);

		return hideNode;
	}

	@Override
	protected String getName() {
		return "hide";
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
