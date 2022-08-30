package com.bendertales.mc.chatapi.command.nodes.root;

import java.util.List;

import com.bendertales.mc.chatapi.command.nodes.channel.*;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.talesservercommon.commands.AbstractIntermediaryCommandNode;


public class NodeChannel extends AbstractIntermediaryCommandNode {

	public NodeChannel(ChatManager chatManager) {
		super(List.of(
			new NodeList(chatManager),
			new NodeHide(chatManager),
			new NodeMute(chatManager),
			new NodeUnmute(chatManager),
			new NodeSelect(chatManager),
			new NodeSocialSpy(chatManager)
		));
	}

	@Override
	protected String getName() {
		return "channel";
	}
}
