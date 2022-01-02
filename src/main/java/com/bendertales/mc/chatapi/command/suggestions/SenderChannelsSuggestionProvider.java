package com.bendertales.mc.chatapi.command.suggestions;

import java.util.concurrent.CompletableFuture;

import com.bendertales.mc.chatapi.impl.ChatManager;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;


public class SenderChannelsSuggestionProvider implements SuggestionProvider<ServerCommandSource> {

	private final ChatManager chatManager;

	public SenderChannelsSuggestionProvider(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context,
	                                                     SuggestionsBuilder builder) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayer();

		var channels = chatManager.getChannels();

		return CommandSource.suggestMatching(
			channels.stream()
			        .filter(ch -> ch.senderFilter().test(player))
			        .map(ch -> ch.id().toString()),
			builder
		);
	}
}
