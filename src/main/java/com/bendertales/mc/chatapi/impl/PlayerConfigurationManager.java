package com.bendertales.mc.chatapi.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.config.PlayerConfiguration;
import com.bendertales.mc.chatapi.config.serialization.IdentifierSerializer;
import com.bendertales.mc.chatapi.impl.vo.Channel;
import com.bendertales.mc.chatapi.impl.vo.PlayerSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;


public class PlayerConfigurationManager {

	private final Map<UUID, PlayerSettings> playersSettingsById = new HashMap<>();
	private final Path configFolder;

	private Identifier defaultChannel;

	public PlayerConfigurationManager() {
		var modContainer = FabricLoader.getInstance().getModContainer(ChatConstants.MODID).orElseThrow();
		configFolder = modContainer.getRootPath().resolve("players");

	}

	public PlayerSettings getOrCreatePlayerSettings(ServerPlayerEntity player) {
		return playersSettingsById.computeIfAbsent(player.getUuid(), id -> {

			var playerConfiguration = tryLoadPlayerConfiguration(player);

			var settings = new PlayerSettings(id);
			settings.setCurrentChannel(playerConfiguration.getActiveChannel());
			settings.muteChannelsById(playerConfiguration.getMutedChannels());
			return settings;
		});
	}

	public void clearSettings() {
		playersSettingsById.clear();
	}

	public void setDefaultChannel(Identifier defaultChannel) {
		this.defaultChannel = defaultChannel;
	}

	public void changeActiveChannel(ServerPlayerEntity player, Channel channel) {
		var settings = getOrCreatePlayerSettings(player);
		settings.setCurrentChannel(channel.id());
		trySavePlayerConfiguration(settings);
	}

	@NotNull
	private PlayerConfiguration defaultConfiguration() {
		var playerConfiguration = new PlayerConfiguration();
		playerConfiguration.setActiveChannel(defaultChannel);
		playerConfiguration.setMutedChannels(Collections.emptySet());
		return playerConfiguration;
	}

	public boolean hasPlayerEnabledSocialSpy(ServerPlayerEntity player) {
		var settings = getOrCreatePlayerSettings(player);
		return settings.isEnabledSocialSpy();
	}

	public boolean isPlayerMutedInChannels(ServerPlayerEntity player, Channel channel) {
		var settings = getOrCreatePlayerSettings(player);
		return settings.isMutedInChannel(channel);
	}

	public void mutePlayerInChannels(ServerPlayerEntity player, Collection<Channel> channelsToMute) {
		var settings = getOrCreatePlayerSettings(player);
		settings.muteChannels(channelsToMute);
		trySavePlayerConfiguration(settings);
	}

	public void unmutePlayerInChannels(ServerPlayerEntity player, Collection<Channel> channels) {
		var settings = getOrCreatePlayerSettings(player);
		settings.unmuteChannels(channels);
		trySavePlayerConfiguration(settings);
	}

	public boolean isChannelHiddenForPlayer(Channel channel, ServerPlayerEntity player) {
		var settings = getOrCreatePlayerSettings(player);
		return settings.isChannelHidden(channel);
	}

	public boolean toggleHiddenChannelForPlayer(Channel channel, ServerPlayerEntity player) {
		var settings = getOrCreatePlayerSettings(player);
		return settings.toggleHiddenChannel(channel);
	}

	public void enableSocialSpy(ServerPlayerEntity player) {
		var settings = getOrCreatePlayerSettings(player);
		settings.setEnabledSocialSpy(true);
	}

	public void disableSocialSpy(ServerPlayerEntity player) {
		var settings = getOrCreatePlayerSettings(player);
		settings.setEnabledSocialSpy(false);
	}

	private PlayerConfiguration tryLoadPlayerConfiguration(ServerPlayerEntity player) {
		try {
			return loadPlayerConfiguration(player);
		}
		catch (IOException e) {
			return defaultConfiguration();
		}
	}


	private PlayerConfiguration loadPlayerConfiguration(ServerPlayerEntity player) throws IOException {
		var playerFile = configFolder.resolve(player.getUuid().toString() + ".json");
		if (Files.exists(playerFile)) {
			var playerJson = Files.readString(playerFile);
			var gson = getGson();
			return gson.fromJson(playerJson, PlayerConfiguration.class);
		}

		return defaultConfiguration();
	}

	public void trySavePlayerConfiguration(PlayerSettings playerSettings) {
		try {
			savePlayerConfiguration(playerSettings);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void savePlayerConfiguration(PlayerSettings playerSettings) throws IOException {
		var playerFile = configFolder.resolve(playerSettings.getPlayerUuid().toString() + ".json");

		var playerConfiguration = new PlayerConfiguration();
		playerConfiguration.setActiveChannel(playerSettings.getCurrentChannel());
		playerConfiguration.setMutedChannels(playerSettings.getMutedChannels());

		var gson = getGson();
		var json = gson.toJson(playerConfiguration);
		Files.writeString(playerFile, json);
	}

	@NotNull
	private Gson getGson() {
		return new GsonBuilder()
			.setPrettyPrinting()
            .registerTypeAdapter(Identifier.class, new IdentifierSerializer())
            .create();
	}
}
