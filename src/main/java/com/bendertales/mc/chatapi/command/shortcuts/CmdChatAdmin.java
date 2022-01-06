package com.bendertales.mc.chatapi.command.shortcuts;

import java.util.Collection;
import java.util.List;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.chatapi.impl.channels.AdminChannel;
import net.minecraft.util.Identifier;


public class CmdChatAdmin extends ShortcutModCommand {

	public CmdChatAdmin(ChatManager chatManager) {
		super(chatManager);
	}

	@Override
	public Collection<String> getRequiredPermissions() {
		return List.of(AdminChannel.PERMISSION);
	}

	@Override
	protected String getCommandRoot() {
		return "cadm";
	}

	@Override
	protected Identifier getChannelId() {
		return ChatConstants.Ids.Channels.ADMIN;
	}
}
