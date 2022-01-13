package com.bendertales.mc.chatapi.impl.messages;

import com.bendertales.mc.chatapi.impl.vo.MessageOptions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


public class DefinitiveFormattedMessage implements FormattedMessage {

	private final Text text;
	private final Text spyText;

	public DefinitiveFormattedMessage(String line) {
		this.text = Text.of(line);
		this.spyText = Text.of("§m*§r" + line);
	}

	@Override
	public Text forRecipient(ServerPlayerEntity recipient, MessageOptions options) {
		if (options.socialSpy()) {
			return spyText;
		}
		return this.text;
	}
}
