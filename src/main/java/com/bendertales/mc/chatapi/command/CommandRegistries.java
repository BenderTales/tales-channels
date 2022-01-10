package com.bendertales.mc.chatapi.command;

import java.util.stream.Stream;

import com.bendertales.mc.chatapi.command.shortcuts.CmdChatAdmin;
import com.bendertales.mc.chatapi.command.shortcuts.CmdChatHelpers;
import com.bendertales.mc.chatapi.command.shortcuts.CmdChatModeration;
import com.bendertales.mc.chatapi.command.shortcuts.CmdChatStaff;
import com.bendertales.mc.chatapi.impl.ChatManager;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;



public class CommandRegistries {

	public static void registerCommands(ChatManager chatManager) {
		var commandRegistry = CommandRegistrationCallback.EVENT;

		buildCommands(chatManager).forEach(commandRegistry::register);
	}

	private static Stream<ModCommand> buildCommands(ChatManager chatManager) {
		return Stream.of(
			new CmdReload(chatManager),
			new CmdList(chatManager),
			new CmdSocialSpy(chatManager),
			new CmdTarget(chatManager),
			new CmdChat(chatManager),
			new CmdHideToggle(chatManager),
			new CmdMute(chatManager),
			new CmdUnmute(chatManager),
			new CmdChatAdmin(chatManager),
			new CmdChatHelpers(chatManager),
			new CmdChatStaff(chatManager),
			new CmdChatModeration(chatManager),
			new CmdPrivateMessage(chatManager)
		);
	}

}
