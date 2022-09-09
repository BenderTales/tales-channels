package fr.bendertales.mc.channels.impl.channels;

import java.util.function.Predicate;

import fr.bendertales.mc.channels.ModConstants;
import fr.bendertales.mc.channels.api.MessageVisibility;
import fr.bendertales.mc.channels.api.ModChannelImplementationsProvider;
import fr.bendertales.mc.channels.api.RecipientFilter;
import fr.bendertales.mc.channels.impl.ChatManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class LocalChannel implements ModChannelImplementationsProvider {

	private final ChatManager chatManager;

	public LocalChannel(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	@Override
	public String getPrefixSelector() {
		return ".";
	}

	@Override
	public String getDefaultFormat() {
		return "[.]%SENDER%> %MESSAGE%";
	}

	@Override
	public Predicate<ServerPlayerEntity> getSenderFilter() {
		return (p) -> true;
	}

	@Override
	public RecipientFilter getRecipientsFilter() {
		return (sender, recipient, options) -> {
			if (sender.equals(recipient)
			    || (sender.getWorld().equals(recipient.getWorld())
		            && sender.getBlockPos().isWithinDistance(recipient.getBlockPos(),chatManager.getLocalChannelDistance()))) {
				return MessageVisibility.SHOW;
			}

			if (options.isRecipientSocialSpy()) {
				return MessageVisibility.SOCIAL_SPY;
			}

			return MessageVisibility.HIDE;
		};
	}

	@Override
	public Identifier getId() {
		return ModConstants.Ids.Channels.LOCAL;
	}
}
