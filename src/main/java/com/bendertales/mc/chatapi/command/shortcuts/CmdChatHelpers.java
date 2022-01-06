package com.bendertales.mc.chatapi.command.shortcuts;

import java.util.Collection;
import java.util.List;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.MessageSender;
import com.bendertales.mc.chatapi.impl.channels.HelpersChannel;
import net.minecraft.util.Identifier;


public class CmdChatHelpers extends ShortcutModCommand{

	public CmdChatHelpers(MessageSender messageSender) {
		super(messageSender);
	}

	@Override
	public Collection<String> getRequiredPermissions() {
		return List.of(HelpersChannel.PERMISSION);
	}

	@Override
	protected String getCommandRoot() {
		return "chel";
	}

	@Override
	protected Identifier getChannelId() {
		return ChatConstants.Ids.Channels.HELPERS;
	}
}
