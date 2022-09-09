package fr.bendertales.mc.channels.impl.vo;

public record PlayerChannelStatus(
	Channel channel,
	boolean selected,
	boolean hidden
) {

}
