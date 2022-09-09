package fr.bendertales.mc.chatapi.impl.vo;

public record PlayerChannelStatus(
	Channel channel,
	boolean selected,
	boolean hidden
) {

}
