package com.bendertales.mc.chatapi.config;

import net.minecraft.util.Identifier;


public class PlayerConfiguration {

	private Identifier activeChannel;

	public Identifier getActiveChannel() {
		return activeChannel;
	}

	public void setActiveChannel(Identifier activeChannel) {
		this.activeChannel = activeChannel;
	}

}
