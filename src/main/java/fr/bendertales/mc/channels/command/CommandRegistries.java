package fr.bendertales.mc.channels.command;

import java.util.List;
import java.util.stream.Stream;

import fr.bendertales.mc.channels.ModConstants;
import fr.bendertales.mc.channels.command.nodes.root.*;
import fr.bendertales.mc.channels.impl.ChatManager;
import fr.bendertales.mc.channels.impl.channels.AdminChannel;
import fr.bendertales.mc.channels.impl.channels.HelpersChannel;
import fr.bendertales.mc.channels.impl.channels.ModerationChannel;
import fr.bendertales.mc.channels.impl.channels.StaffChannel;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;


public class CommandRegistries {

	public static void registerCommands(ChatManager chatManager) {
		var commandRegistry = CommandRegistrationCallback.EVENT;

		commandRegistry.register((dispatcher, ra, env) -> {
			NativeCommandsClearer.clear(dispatcher);
			buildCommandsNodes(chatManager)
				.forEach(command -> dispatcher.register(command.asBrigadierNode()));
		});

	}

	private static Stream<TalesCommandNode> buildCommandsNodes(ChatManager chatManager) {
		return Stream.of(
			new NodeChannel(chatManager),
			new NodeChat(chatManager),
			new NodeChatapi(chatManager),
			new NodeMessage(chatManager),
			new NodeRespond(chatManager),
			new NodeShortcut("cadm", List.of(AdminChannel.PERMISSION), chatManager, ModConstants.Ids.Channels.ADMIN),
			new NodeShortcut("chel", List.of(HelpersChannel.PERMISSION), chatManager, ModConstants.Ids.Channels.HELPERS),
			new NodeShortcut("cmod", List.of(ModerationChannel.PERMISSION), chatManager, ModConstants.Ids.Channels.MODO),
			new NodeShortcut("cstf", List.of(StaffChannel.PERMISSION), chatManager, ModConstants.Ids.Channels.STAFF)
		);
	}

}
