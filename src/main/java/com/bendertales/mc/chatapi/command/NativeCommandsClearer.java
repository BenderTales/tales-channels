package com.bendertales.mc.chatapi.command;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.server.command.ServerCommandSource;


public class NativeCommandsClearer {

	private static final List<String> commandsToRemove = List.of("msg", "say", "me", "tell", "w");

	private NativeCommandsClearer() {}

	public static void clear(CommandDispatcher<ServerCommandSource> dispatcher) {
		var commandsRoot = dispatcher.getRoot();
		Class<? extends RootCommandNode> rootCmdClass = commandsRoot.getClass();
		Class<?> cmdClass = rootCmdClass.getSuperclass();

		Arrays.stream(cmdClass.getDeclaredFields())
            .filter(field -> {
                var type = field.getGenericType();
			    if (type instanceof ParameterizedType paramType) {
				    if (paramType.getRawType() != Map.class) {
					    return false;
				    }
				    var genTypes = paramType.getActualTypeArguments();
				    if (genTypes[0] != String.class) {
					    return false;
				    }

				    if (genTypes[1] instanceof ParameterizedType genParamType) {
					    var rawType = genParamType.getRawType();
					    if (rawType == CommandNode.class || rawType == LiteralCommandNode.class) {
							return true;
						}
				    }

				    return false;
			    }
				return false;
            })
            .forEach(field -> {
				field.setAccessible(true);
	            try {
		            var children = (Map<String, ?>)field.get(commandsRoot);
					commandsToRemove.forEach(children::remove);
	            }
	            catch (IllegalAccessException e) {
		            e.printStackTrace();
	            }
				finally {
					field.setAccessible(false);
	            }
            });
	}
}
