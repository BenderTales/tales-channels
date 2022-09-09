package fr.bendertales.mc.chatapi.command.nodes.channel;

import java.util.List;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.bendertales.mc.chatapi.command.subcommands.CmdChannelSocialSpy;
import fr.bendertales.mc.chatapi.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;


public class NodeSocialSpy implements TalesCommandNode {

	private final List<String>            permissions  = List.of("chatapi.commands.admin", "chatapi.commands.socialspy");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_SENIOR, permissions);

	private final CmdChannelSocialSpy cmdSocialSpy;

	public NodeSocialSpy(ChatManager chatManager) {
		cmdSocialSpy = new CmdChannelSocialSpy(chatManager);
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("channel")
		       .then(literal("socialspy")
	                .requires(getRequirements().asPredicate())
	                .then(literal("on")
	                    .executes(cmdSocialSpy::enableSocialSpy))
	                .then(literal("off")
                        .executes(cmdSocialSpy::disableSocialSpy)));
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
