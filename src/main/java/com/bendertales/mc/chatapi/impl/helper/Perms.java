package com.bendertales.mc.chatapi.impl.helper;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.network.ServerPlayerEntity;


public final class Perms {

	public static boolean hasAny(ServerPlayerEntity player, String... permissions) {
		for (var permission : permissions) {
			if (Permissions.check(player, permission)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isOp(ServerPlayerEntity player) {
		return player.hasPermissionLevel(3);
	}

	private Perms() {
	}
}
