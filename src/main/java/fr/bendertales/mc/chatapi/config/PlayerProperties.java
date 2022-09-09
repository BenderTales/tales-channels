package fr.bendertales.mc.chatapi.config;

import java.util.Set;

import net.minecraft.util.Identifier;


public class PlayerProperties {

	private Identifier activeChannel;
	private Set<Identifier> mutedChannels;

	public Identifier getActiveChannel() {
		return activeChannel;
	}

	public void setActiveChannel(Identifier activeChannel) {
		this.activeChannel = activeChannel;
	}

	public Set<Identifier> getMutedChannels() {
		return mutedChannels;
	}

	public void setMutedChannels(Set<Identifier> mutedChannels) {
		this.mutedChannels = mutedChannels;
	}
}
