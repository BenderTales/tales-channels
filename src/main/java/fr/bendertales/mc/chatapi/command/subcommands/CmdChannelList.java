package fr.bendertales.mc.chatapi.command.subcommands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.chatapi.impl.ChatManager;
import fr.bendertales.mc.chatapi.impl.vo.Channel;
import fr.bendertales.mc.chatapi.impl.vo.PlayerChannelStatus;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


public class CmdChannelList {

	private final ChatManager chatManager;

	public CmdChannelList(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var cmdSource = context.getSource();
		var player = cmdSource.getPlayer();
		if (player != null) {
			return listToPlayer(player);
		}

		return listToServer(cmdSource);
	}

	private int listToServer(ServerCommandSource commandSource) {
		var channels = chatManager.getChannels();

		for (Channel channel : channels) {
			commandSource.sendFeedback(Text.of(channel.id().toString()), false);
		}

		return channels.size();
	}

	private int listToPlayer(ServerPlayerEntity player) {
		var channelsStatus = chatManager.getPlayerChannelsStatus(player);

		for (PlayerChannelStatus status : channelsStatus) {
			var channelName = status.channel().id().toString();
			String firstColor = status.selected() ? "§3" : "§f";
			var hideStatus = status.hidden() ? "§cHidden" : "§aVisible";
			player.sendMessage(Text.of(firstColor + channelName + ": " + hideStatus), false);
		}

		return channelsStatus.size();
	}
}
