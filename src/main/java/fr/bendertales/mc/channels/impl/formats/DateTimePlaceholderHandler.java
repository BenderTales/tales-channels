package fr.bendertales.mc.channels.impl.formats;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import fr.bendertales.mc.channels.ModConstants;
import fr.bendertales.mc.channels.api.AbstractPlaceholderHandler;
import fr.bendertales.mc.channels.api.PlaceholderFormatter;
import fr.bendertales.mc.channels.api.PlaceholderHandler;
import net.minecraft.util.Identifier;


public class DateTimePlaceholderHandler extends AbstractPlaceholderHandler
		implements PlaceholderHandler {

	private static final String DATETIME_PLACEHOLDER = "%HHMM%";

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

	public DateTimePlaceholderHandler() {
		super(DATETIME_PLACEHOLDER);
	}

	@Override
	public PlaceholderFormatter getPlaceholderFormatter() {
		return (message) -> {
			var now = LocalTime.now();
			return now.format(dateTimeFormatter);
		};
	}

	@Override
	public Identifier getId() {
		return ModConstants.Ids.Formats.TIME;
	}
}
