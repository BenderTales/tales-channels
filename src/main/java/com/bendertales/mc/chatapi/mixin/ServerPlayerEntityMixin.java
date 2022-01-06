package com.bendertales.mc.chatapi.mixin;

import com.bendertales.mc.chatapi.impl.vo.ChatApiConfigurable;
import com.bendertales.mc.chatapi.impl.vo.ChatApiPlayerSettings;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ChatApiConfigurable {

	private final ChatApiPlayerSettings chatApiSettings = new ChatApiPlayerSettings();

	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	@Override
	public ChatApiPlayerSettings getChatApiPlayerSettings() {
		return chatApiSettings;
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		var chatCompound = new NbtCompound();
		chatApiSettings.writeDataToNbt(chatCompound);
		nbt.put("chat-api-settings", chatCompound);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("chat-api-settings")) {
			var chatCompound = nbt.getCompound("chat-api-settings");
			chatApiSettings.readDataFromNbt(chatCompound);
		}
	}

}
