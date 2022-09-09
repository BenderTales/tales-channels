package fr.bendertales.mc.channels.impl.messages;


import fr.bendertales.mc.channels.api.PlaceholderFormatter;


record IndexedPlaceholderFormatter(
		String key,
		PlaceholderFormatter formatter,
		int index,
		int length
) {
}
