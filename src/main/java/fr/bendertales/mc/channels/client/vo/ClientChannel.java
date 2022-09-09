package fr.bendertales.mc.channels.client.vo;

import net.minecraft.util.Identifier;


public class ClientChannel {

	private final Identifier identifier;
	private boolean target;
	private boolean hidden;

	public ClientChannel(Identifier identifier) {
		this.identifier = identifier;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public boolean isTarget() {
		return target;
	}

	public ClientChannel target(boolean target) {
		this.target = target;
		return this;
	}

	public void setTarget(boolean target) {
		this.target = target;
	}

	public boolean isHidden() {
		return hidden;
	}

	public ClientChannel hidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
