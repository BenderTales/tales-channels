package com.bendertales.mc.chatapi.client.gui;

import java.util.function.BiConsumer;

import com.bendertales.mc.chatapi.client.vo.ClientChannel;


public class ChannelsWidgetConfigurator implements BiConsumer<ClientChannel, ChannelWidget> {

	@Override
	public void accept(ClientChannel channel, ChannelWidget widget) {
		widget.setIdentifier(channel.getIdentifier());
		widget.setHidden(channel.isHidden());
		widget.setTarget(channel.isTarget());
	}

}
