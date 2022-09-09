package fr.bendertales.mc.channels.command.nodes.root;

import java.util.List;

import fr.bendertales.mc.channels.command.nodes.chatapi.NodeReload;
import fr.bendertales.mc.channels.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.AbstractIntermediaryCommandNode;


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
