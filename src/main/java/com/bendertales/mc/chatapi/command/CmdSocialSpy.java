package com.bendertales.mc.chatapi.command;

import java.util.Collection;
import java.util.List;

import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;


public class CmdSocialSpy implements ModCommand {

	private final ChatManager chatManager;

	public CmdSocialSpy(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		dispatcher.register(
			literal("channel")
				.then(literal("socialspy")
	                .requires(getRequirements())
			        .then(literal("on")
		                .executes(this))
			        .then(literal("off")
		                .executes(this::disableSocialSpy)))
		);
	}

	@Override
	public Collection<String> getRequiredPermissions() {
		return List.of("chatapi.commands.admin", "chatapi.commands.socialspy");
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayer();
		chatManager.enableSocialSpy(player);
		cmdSource.sendFeedback(new LiteralText("Social spy enabled").formatted(Formatting.GREEN), true);
		return 0;
	}

	public int disableSocialSpy(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayer();
		chatManager.disableSocialSpy(player);
		cmdSource.sendFeedback(new LiteralText("Social spy disabled").formatted(Formatting.GOLD), true);
		return 0;
	}

}
