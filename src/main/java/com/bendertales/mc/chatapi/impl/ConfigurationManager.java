package com.bendertales.mc.chatapi.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.api.ChannelDefault;
import com.bendertales.mc.chatapi.api.PlaceholderHandler;
import com.bendertales.mc.chatapi.api.Registry;
import com.bendertales.mc.chatapi.config.ChannelProperties;
import com.bendertales.mc.chatapi.config.ModConfiguration;
import com.bendertales.mc.chatapi.config.PlaceholderProperties;
import com.bendertales.mc.chatapi.config.serialization.IdentifierSerializer;
import com.bendertales.mc.chatapi.impl.vo.Channel;
import com.bendertales.mc.chatapi.impl.vo.Placeholder;
import com.bendertales.mc.chatapi.impl.vo.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;


public class ConfigurationManager {

	private final Path configFile;

	public ConfigurationManager() {
		configFile = FabricLoader.getInstance().getConfigDir()
		                         .resolve(ChatConstants.MODID).resolve("config.json");
	}

	public Settings buildConfiguredChannels() {

		var modConfiguration = tryReadConfiguration();
		updateFileIfNecessary(modConfiguration);

		var placeholdersById = extractSortedPlaceholders(modConfiguration);

		var channels = new Object2ObjectOpenHashMap<Identifier, Channel>();

		modConfiguration.getChannels().entrySet().stream()
            .map(e -> new ChannelStruct(e.getKey(), e.getValue().isDisabled(), e.getValue().getFormat()))
			.filter(c -> !c.disabled())
			.map(c -> {
				var channelDefault = Registry.CHANNEL_HANDLERS.get(c.id());
				var format = c.format();

				var channelPlaceholders = extractNecessaryPlaceholders(placeholdersById, format);

				return new Channel(channelDefault.getId(), channelDefault.getPrefixSelector(),
				                          format, channelPlaceholders,
				                          channelDefault.getRecipientsFilter(), channelDefault.getSenderFilter());
			})
            .forEach(ch -> channels.put(ch.id(), ch));

		return new Settings(modConfiguration.getDefaultChannel(), modConfiguration.getLocalChannelDistance(), channels);
	}

	private List<Placeholder> extractNecessaryPlaceholders(Map<Identifier, Placeholder> placeholdersById, String format) {
		return Registry.FORMAT_HANDLERS.stream()
            .filter(ph -> ph.shouldApplyFormat(format))
            .map(ph -> placeholdersById.get(ph.getId()))
            .sorted(Comparator.comparingInt(Placeholder::applyOrder))
            .toList();
	}

	private ModConfiguration tryReadConfiguration() {
		try {
			return readConfiguration();
		}
		catch (IOException e) {
			e.printStackTrace();
			return defaultConfiguration();
		}
	}

	private ModConfiguration readConfiguration() throws IOException {
		if (!Files.exists(configFile)) {
			return defaultConfiguration();
		}

		var fileContent = Files.readString(configFile);
		Gson gson = getGson();
		return gson.fromJson(fileContent, ModConfiguration.class);
	}

	private ModConfiguration defaultConfiguration() {
		var modConfiguration = new ModConfiguration();
		modConfiguration.setLocalChannelDistance(40);
		modConfiguration.setDefaultChannel(ChatConstants.Ids.Channels.GLOBAL);
		modConfiguration.setChannels(new Object2ObjectOpenHashMap<>());
		modConfiguration.setPlaceholders(new Object2ObjectOpenHashMap<>());
		return modConfiguration;
	}

	@NotNull
	private Map<Identifier, Placeholder> extractSortedPlaceholders(ModConfiguration modConfiguration) {
		return modConfiguration.getPlaceholders()
            .entrySet()
            .stream()
            .map(e -> {
	            var ph = Registry.FORMAT_HANDLERS.get(e.getKey());
	            return new Placeholder(ph.getId(), e.getValue().getApplicationOrder(), ph.getMessageFormatter());
            })
            .collect(Collectors.toMap(Placeholder::id, p -> p));
	}

	private void updateFileIfNecessary(ModConfiguration modConfiguration) {
		boolean changedConfiguration = false;

		if (modConfiguration.getLocalChannelDistance() < 4) {
			modConfiguration.setLocalChannelDistance(4);
			changedConfiguration = true;
		}

		var configPlaceholders = modConfiguration.getPlaceholders();
		for (PlaceholderHandler placeholderHandler : Registry.FORMAT_HANDLERS) {
			var placeholderId = placeholderHandler.getId();
			if (!configPlaceholders.containsKey(placeholderId)) {
				var placeholderProperties = new PlaceholderProperties();
				placeholderProperties.setApplicationOrder(placeholderHandler.getDefaultPriorityOrder());
				configPlaceholders.put(placeholderId, placeholderProperties);
				changedConfiguration = true;
			}
		}

		var configChannels = modConfiguration.getChannels();
		for (ChannelDefault channelDefault : Registry.CHANNEL_HANDLERS) {
			var channelId = channelDefault.getId();
			if (!configChannels.containsKey(channelId)) {
				var channelProperties = new ChannelProperties();
				channelProperties.setDisabled(!channelDefault.isEnabledByDefault());
				channelProperties.setFormat(channelDefault.getDefaultFormat());
				configChannels.put(channelId, channelProperties);
				changedConfiguration = true;
			}
		}

		if (changedConfiguration) {
			tryWriteConfiguration(modConfiguration);
		}
	}

	private void tryWriteConfiguration(ModConfiguration modConfiguration) {
		try {
			writeConfiguration(modConfiguration);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeConfiguration(ModConfiguration modConfiguration) throws IOException {
		if (!Files.exists(configFile)) {
			Files.createDirectories(configFile.getParent());
		}

		var gson = getGson();
		var configurationJson = gson.toJson(modConfiguration, ModConfiguration.class);
		Files.writeString(configFile, configurationJson);
	}

	@NotNull
	private Gson getGson() {
		return new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Identifier.class, new IdentifierSerializer())
				.create();
	}

	private record ChannelStruct(
		Identifier id,
		boolean disabled,
		String format
	){}

}
