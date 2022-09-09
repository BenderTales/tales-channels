package fr.bendertales.mc.chatapi.mixin;


import fr.bendertales.mc.channels.api.ChatException;
import fr.bendertales.mc.chatapi.impl.ChatManager;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

	@Inject(method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V", at = @At(value = "HEAD"), cancellable = true)
	private void replaceChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params, CallbackInfo ci) {
		try {
			var messageContent = message.getContent().getString();
			ChatManager.get().handleMessage(sender, messageContent);
		}
		catch (ChatException e) {
			sender.sendMessage(Text.literal(e.getMessage()).formatted(Formatting.RED), false);
		}
		ci.cancel();
	}

}
