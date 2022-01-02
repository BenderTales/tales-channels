package com.bendertales.mc.chatapi.client.gui;

import java.util.List;

import com.bendertales.mc.chatapi.client.vo.ClientChannel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;


public class ChannelsListPanel extends WListPanel<ClientChannel, ChannelWidget> {
	/**
	 * Constructs a list panel.
	 *
	 * @param data
	 * 		the list data
	 */
	public ChannelsListPanel(List<ClientChannel> data) {
		super(data, ChannelWidget::new, new ChannelsWidgetConfigurator());
		super.cellHeight = 20;
		super.margin = 2;
	}
}
