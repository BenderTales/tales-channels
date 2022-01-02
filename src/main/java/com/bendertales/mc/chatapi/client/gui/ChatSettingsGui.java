package com.bendertales.mc.chatapi.client.gui;

import java.util.List;

import com.bendertales.mc.chatapi.client.vo.ClientChannel;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class ChatSettingsGui extends LightweightGuiDescription {

	private List<ClientChannel> channels = List.of(
		new ClientChannel(new Identifier("test", "1")).hidden(true).target(false),
		new ClientChannel(new Identifier("test", "2")).hidden(false).target(true),
		new ClientChannel(new Identifier("test", "3")).hidden(false).target(false),
		new ClientChannel(new Identifier("test", "4")).hidden(false).target(false),
		new ClientChannel(new Identifier("test", "5")).hidden(false).target(false),
		new ClientChannel(new Identifier("test", "6")).hidden(false).target(false),
		new ClientChannel(new Identifier("test", "7")).hidden(false).target(false),
		new ClientChannel(new Identifier("test", "8")).hidden(false).target(false),
		new ClientChannel(new Identifier("test", "9")).hidden(false).target(false)
	);

	public ChatSettingsGui() {

		var headers = new WBox(Axis.HORIZONTAL);

		WLabel selected = new WLabel(Text.of("Selected"));
		headers.add(selected, 50, 20);

		WLabel hidden = new WLabel(Text.of("Hidden"));
		headers.add(hidden, 50, 20);

		WLabel identifier = new WLabel(Text.of("Identifer"));
		headers.add(identifier, 50, 20);

		ChannelsListPanel channelsPanel = new ChannelsListPanel(channels);

		var root = new WBox(Axis.VERTICAL);
		setRootPanel(root);
		root.setSize(256, 240);
		root.setInsets(Insets.ROOT_PANEL);
		root.add(headers);
		root.add(channelsPanel, 256, 126);

		root.validate(this);
	}
}
