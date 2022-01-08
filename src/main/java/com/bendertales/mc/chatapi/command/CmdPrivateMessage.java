package com.bendertales.mc.chatapi.command;

import java.util.Collection;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;


public class CmdPrivateMessage implements ModCommand{

	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {

	}

	@Override
	public Collection<String> getRequiredPermissions() {
		return List.of("chatapi.commands.admin", "chatapi.commands.private_message");
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return 0;
	}


}
