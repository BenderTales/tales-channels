package fr.bendertales.mc.chatapi.impl.messages;

import fr.bendertales.mc.chatapi.impl.vo.MessageOptions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


public interface FormattedMessage {

	Text forRecipient(ServerPlayerEntity recipient, MessageOptions options);
}
