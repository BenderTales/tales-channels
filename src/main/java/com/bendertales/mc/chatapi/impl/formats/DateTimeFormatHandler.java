package com.bendertales.mc.chatapi.impl.formats;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.FormatHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class DateTimeFormatHandler implements FormatHandler {

	private static final String HOUR_PLACEHOLDER = "%HH%";
	private static final String MINUTES_PLACEHOLDER = "%mm%";
	private static final String SECONDS_PLACEHOLDER = "%SS%";

	private final DateTimeFormatter hoursFormatter = DateTimeFormatter.ofPattern("HH");
	private final DateTimeFormatter minutesFormatter = DateTimeFormatter.ofPattern("mm");
	private final DateTimeFormatter secondsFormatter = DateTimeFormatter.ofPattern("SS");



	@Override
	public int getDefaultPriorityOrder() {
		return 1;
	}

	@Override
	public boolean shouldApplyFormat(String format) {
		return format.contains(HOUR_PLACEHOLDER)
		       || format.contains(MINUTES_PLACEHOLDER)
		       || format.contains(SECONDS_PLACEHOLDER);
	}

	@Override
	public String handleMessage(String format, ServerPlayerEntity player, String message) {
		var now = LocalTime.now();
		return format.replace(HOUR_PLACEHOLDER, now.format(hoursFormatter))
		             .replace(MINUTES_PLACEHOLDER, now.format(minutesFormatter))
		             .replace(SECONDS_PLACEHOLDER, now.format(secondsFormatter));
	}

	@Override
	public Identifier getId() {
		return ChatConstants.Ids.Formats.TIME;
	}
}
