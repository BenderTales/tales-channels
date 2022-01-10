package com.bendertales.mc.chatapi.impl.messages;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


public class DefinitiveFormattedMessage implements FormattedMessage {

	private final Text text;

	public DefinitiveFormattedMessage(String line) {
		this.text = Text.of(line);
	}

	@Override
	public Text forRecipient(ServerPlayerEntity recipient) {
		return this.text;
	}
}
