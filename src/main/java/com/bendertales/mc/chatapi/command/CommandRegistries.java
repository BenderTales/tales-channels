package com.bendertales.mc.chatapi.command;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.helper.Perms;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;


public class CommandRegistries {

	public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, ChatManager chatManager) {
		var chatNode = literal("chat").build();

		var listNode = literal("list")
			.requires(permission(List.of("chat.command.list")))
            .executes(new CmdList(chatManager))
            .build();

		var muteNode = literal("mute").build();

		var muteListNode = literal("list")
				.requires(permission(List.of("chat.command.mute.list")))
				.executes(new CmdMuteList(chatManager))
				.build();

		var muteToggleNode = literal("toggle")
				.requires(permission(List.of("chat.command.mute.toggle")))
				.executes(new CmdMuteToggle(chatManager))
				.build();

		var selectNode = literal("select")
				.requires(permission(List.of("chat.command.select")))
				.executes(new CmdSelect(chatManager))
				.build();

		chatNode.addChild(listNode);
		chatNode.addChild(selectNode);
		chatNode.addChild(muteNode);

		muteNode.addChild(muteListNode);
		muteNode.addChild(muteToggleNode);

		dispatcher.getRoot().addChild(chatNode);
	}

	private static Predicate<ServerCommandSource> permission(Collection<String> permissions) {
		return cmdSource -> Perms.isOp(cmdSource) || Perms.hasAny(cmdSource, permissions);
	}
}
