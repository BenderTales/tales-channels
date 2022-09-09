package fr.bendertales.mc.channels.command.nodes.root;

import java.util.List;

import fr.bendertales.mc.channels.command.nodes.channel.*;
import fr.bendertales.mc.channels.impl.ChatManager;
import fr.bendertales.mc.talesservercommon.commands.AbstractIntermediaryCommandNode;


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
