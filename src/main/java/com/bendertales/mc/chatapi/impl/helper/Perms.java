package com.bendertales.mc.chatapi.impl.helper;

import java.util.Collection;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandSource;
import net.minecraft.server.network.ServerPlayerEntity;


public final class Perms {

	public static boolean hasAny(ServerPlayerEntity player, Collection<String> permissions) {
		for (var permission : permissions) {
			if (Permissions.check(player, permission)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasAny(CommandSource commandSource, Collection<String> permissions) {
		for (var permission : permissions) {
			if (Permissions.check(commandSource, permission)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isOp(ServerPlayerEntity player) {
		return player.hasPermissionLevel(3);
	}

	public static boolean isOp(CommandSource commandSource) {
		return commandSource.hasPermissionLevel(3);
	}

	private Perms() {
	}
}
