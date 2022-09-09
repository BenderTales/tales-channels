package fr.bendertales.mc.chatapi.command.nodes.channel;

import java.util.List;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.bendertales.mc.chatapi.command.subcommands.CmdChannelMute;
import fr.bendertales.mc.chatapi.command.suggestions.SenderChannelsSuggestionProvider;
import fr.bendertales.mc.chatapi.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class NodeMute implements TalesCommandNode {

	private final List<String>            permissions  = List.of("chatapi.commands.admin", "chatapi.commands.mute");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_MEDIOR, permissions);

	private final CmdChannelMute                   cmdChannelMute;
	private final SenderChannelsSuggestionProvider suggestionProvider;

	public NodeMute (ChatManager chatManager) {
		this.cmdChannelMute = new CmdChannelMute(chatManager);
		suggestionProvider = new SenderChannelsSuggestionProvider(chatManager);
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("mute")
		       .requires(getRequirements().asPredicate())
		       .then(argument("player", EntityArgumentType.player())
	                .then(literal("*")
                        .executes(cmdChannelMute::runAll))
	                .then(argument("channel", IdentifierArgumentType.identifier())
                        .suggests(suggestionProvider)
                        .executes(cmdChannelMute::run)));
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
