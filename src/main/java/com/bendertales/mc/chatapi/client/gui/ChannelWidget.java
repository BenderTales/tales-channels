package com.bendertales.mc.chatapi.client.gui;

import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class ChannelWidget extends WBox {

	private final WLabel identifier;
	private final WToggleButton hidden;
	private final WToggleButton target;

	public ChannelWidget() {
		super(Axis.HORIZONTAL);

		identifier = new WLabel("").setVerticalAlignment(VerticalAlignment.CENTER);
		hidden = new WToggleButton();
		target = new WToggleButton();

		this.add(target, 50, 20);
		this.add(hidden, 50, 20);
		this.add(identifier);
		this.setSize(200, 20);
	}

	public void setHidden(boolean hidden) {
		this.hidden.setToggle(hidden);
	}

	public void setTarget(boolean target) {
		this.target.setToggle(target);
	}

	public void setIdentifier(Identifier identifier) {
		this.identifier.setText(Text.of(identifier.toString()));
	}
}
