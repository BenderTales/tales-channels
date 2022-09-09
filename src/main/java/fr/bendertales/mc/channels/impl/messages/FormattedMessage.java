package fr.bendertales.mc.channels.impl.messages;

import fr.bendertales.mc.channels.impl.vo.MessageOptions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


public interface FormattedMessage {

	Text forRecipient(ServerPlayerEntity recipient, MessageOptions options);
}
