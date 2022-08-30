package com.bendertales.mc.chatapi.command.nodes.root;

import java.util.List;

import com.bendertales.mc.chatapi.command.nodes.chatapi.NodeReload;
import com.bendertales.mc.chatapi.impl.ChatManager;
import com.bendertales.mc.talesservercommon.commands.AbstractIntermediaryCommandNode;


public class NodeChatapi extends AbstractIntermediaryCommandNode {

	public NodeChatapi(ChatManager chatManager) {
		super(List.of(
			new NodeReload(chatManager)
		));
	}

	@Override
	protected String getName() {
		return "chatapi";
	}
}
