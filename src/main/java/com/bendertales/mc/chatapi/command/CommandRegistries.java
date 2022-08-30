package com.bendertales.mc.chatapi.command;

import java.util.List;
import java.util.stream.Stream;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.command.nodes.root.*;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.channels.AdminChannel;
import com.bendertales.mc.chatapi.impl.channels.HelpersChannel;
import com.bendertales.mc.chatapi.impl.channels.ModerationChannel;
import com.bendertales.mc.chatapi.impl.channels.StaffChannel;
import com.bendertales.mc.talesservercommon.commands.TalesCommandNode;
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
			new NodeShortcut("cadm", List.of(AdminChannel.PERMISSION), chatManager, ChatConstants.Ids.Channels.ADMIN),
			new NodeShortcut("chel", List.of(HelpersChannel.PERMISSION), chatManager, ChatConstants.Ids.Channels.HELPERS),
			new NodeShortcut("cmod", List.of(ModerationChannel.PERMISSION), chatManager, ChatConstants.Ids.Channels.MODO),
			new NodeShortcut("cstf", List.of(StaffChannel.PERMISSION), chatManager, ChatConstants.Ids.Channels.STAFF)
		);
	}

}
