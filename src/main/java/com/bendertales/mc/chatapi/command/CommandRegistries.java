package com.bendertales.mc.chatapi.command;

import java.util.stream.Stream;

import com.bendertales.mc.chatapi.impl.ChatManager;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;



public class CommandRegistries {

	public static void registerCommands(ChatManager chatManager) {
		var commandRegistry = CommandRegistrationCallback.EVENT;

		buildCommands(chatManager).forEach(commandRegistry::register);
	}

	private static Stream<ModCommand> buildCommands(ChatManager chatManager) {
		return Stream.of(
			new CmdList(chatManager),
			new CmdTarget(chatManager),
			new CmdChat(chatManager),
			new CmdHideToggle(chatManager)
		);
	}

}
