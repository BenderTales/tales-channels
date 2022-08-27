package com.bendertales.mc.chatapi.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.bendertales.mc.chatapi.ChatConstants;
import com.bendertales.mc.chatapi.config.PlayerProperties;
import com.bendertales.mc.chatapi.impl.vo.PlayerSettings;
import com.bendertales.mc.talesservercommon.repository.data.AbstractPlayerDataRepository;
import com.bendertales.mc.talesservercommon.repository.serialization.CommonSerializers;
import com.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class PlayerDataRepository
		extends AbstractPlayerDataRepository<PlayerSettings, PlayerProperties> {

	private Identifier defaultChannel;

	public PlayerDataRepository() {
		super(ChatConstants.MODID, PlayerProperties.class);
	}

	public void setDefaultChannel(Identifier defaultChannel) {
		this.defaultChannel = defaultChannel;
	}

	protected List<JsonSerializerRegistration<?>> getSerializers() {
		return List.of(CommonSerializers.identifier());
	}

	@Override
	protected PlayerSettings convert(PlayerProperties original) {
		var settings = new PlayerSettings();
		settings.setCurrentChannel(original.getActiveChannel());
		settings.muteChannelsById(original.getMutedChannels());
		return settings;
	}

	@Override
	protected PlayerProperties deconvert(PlayerSettings playerSettings) {
		var playerConfiguration = new PlayerProperties();
		playerConfiguration.setActiveChannel(playerSettings.getCurrentChannel());
		playerConfiguration.setMutedChannels(playerSettings.getMutedChannels());
		return playerConfiguration;
	}

	@Override
	protected PlayerProperties getDefaultConfiguration() {
		var playerConfiguration = new PlayerProperties();
		playerConfiguration.setActiveChannel(defaultChannel);
		playerConfiguration.setMutedChannels(Collections.emptySet());
		return playerConfiguration;
	}

	@Override
	protected UUID keyToId(ServerPlayerEntity player) {
		return player.getUuid();
	}

}
