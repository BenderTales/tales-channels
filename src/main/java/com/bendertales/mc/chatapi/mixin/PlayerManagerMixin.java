package com.bendertales.mc.chatapi.mixin;


import com.bendertales.mc.chatapi.api.ChatException;
import com.bendertales.mc.chatapi.impl.ChatManager;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.filter.FilteredMessage;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

	@Inject(method = "broadcast(Lnet/minecraft/server/filter/FilteredMessage;Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/util/registry/RegistryKey;)V", at = @At(value = "HEAD"), cancellable = true)
	private void replaceChatMessage(FilteredMessage<SignedMessage> message, ServerCommandSource source, RegistryKey<MessageType> typeKey, CallbackInfo ci) {
		var player = source.getPlayer();
		if (player != null) {
			try {
				var messageContent = message.raw().getContent().getString();
				ChatManager.get().handleMessage(player, messageContent);
			}
			catch (ChatException e) {
				player.sendMessage(Text.literal(e.getMessage()).formatted(Formatting.RED), false);
			}
			ci.cancel();
		}
	}

}
