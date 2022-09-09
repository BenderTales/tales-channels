package fr.bendertales.mc.chatapi.impl;


import java.util.List;

import fr.bendertales.mc.channels.api.ModChannelImplementationsProvider;
import fr.bendertales.mc.channels.api.PlaceholderHandler;
import fr.bendertales.mc.chatapi.ChatConstants;
import fr.bendertales.mc.chatapi.config.ChannelProperties;
import fr.bendertales.mc.chatapi.config.ModProperties;
import fr.bendertales.mc.chatapi.config.PrivateMessageProperties;
import fr.bendertales.mc.chatapi.impl.messages.MessageFormatter;
import fr.bendertales.mc.chatapi.impl.vo.Channel;
import fr.bendertales.mc.chatapi.impl.vo.ModSettings;
import fr.bendertales.mc.chatapi.impl.vo.PrivateMessageFormatters;
import fr.bendertales.mc.talesservercommon.repository.ModPaths;
import fr.bendertales.mc.talesservercommon.repository.config.ConfigRepository;
import fr.bendertales.mc.talesservercommon.repository.serialization.CommonSerializers;
import fr.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;


public final class ModSettingsRepository extends ConfigRepository<ModSettings, ModProperties>{

	public ModSettingsRepository() {
		super(ModProperties.class, ModPaths.createConfigFile(ChatConstants.MODID));
	}

	@Override
	protected ModProperties getDefaultConfiguration() {
		var privateMessageProperties = new PrivateMessageProperties();
		privateMessageProperties.setConsoleFormat("[PM] %SENDER% -> %RECIPIENT%: %MESSAGE%");
		privateMessageProperties.setSenderIsYouFormat("You -> %RECIPIENT%: %MESSAGE%");
		privateMessageProperties.setSenderIsOtherFormat("%SENDER% -> You: %MESSAGE%");

		var modConfiguration = new ModProperties();
		modConfiguration.setLocalChannelDistance(40);
		modConfiguration.setPrivateMessages(privateMessageProperties);
		modConfiguration.setDefaultChannel(ChatConstants.Ids.Channels.GLOBAL);
		modConfiguration.setChannels(new Object2ObjectOpenHashMap<>());
		return modConfiguration;
	}

	protected boolean checkFileContent(ModProperties modProperties) {
		boolean changedConfiguration = false;

		if (modProperties.getLocalChannelDistance() < 4) {
			modProperties.setLocalChannelDistance(4);
			changedConfiguration = true;
		}

		var configChannels = modProperties.getChannels();
		for (ModChannelImplementationsProvider channelDefault : Registry.CHANNEL_HANDLERS) {
			var channelId = channelDefault.getId();
			if (!configChannels.containsKey(channelId)) {
				var channelProperties = new ChannelProperties();
				channelProperties.setDisabled(!channelDefault.isEnabledByDefault());
				channelProperties.setFormat(channelDefault.getDefaultFormat());
				configChannels.put(channelId, channelProperties);
				changedConfiguration = true;
			}
		}

		return changedConfiguration;
	}

	protected ModSettings convert(ModProperties modProperties) {
		var channels = prepareChannels(modProperties);

		var privateMessageFormatters = createPrivateMessageFormatters(modProperties);

		return new ModSettings(modProperties.getDefaultChannel(),
		                       modProperties.getLocalChannelDistance(),
		                       privateMessageFormatters,
		                       channels);
	}

	protected PrivateMessageFormatters createPrivateMessageFormatters(ModProperties modProperties) {
		var pmProps = modProperties.getPrivateMessages();
		return new PrivateMessageFormatters(
			createMessageFormatter(pmProps.getConsoleFormat()),
			createMessageFormatter(pmProps.getSenderIsYouFormat()),
			createMessageFormatter(pmProps.getSenderIsOtherFormat())
		);
	}

	@NotNull
	private static Object2ObjectOpenHashMap<Identifier, Channel> prepareChannels(ModProperties modProperties) {
		var channels = new Object2ObjectOpenHashMap<Identifier, Channel>();

		modProperties.getChannels().entrySet().stream()
            .map(e -> new ChannelStruct(e.getKey(), e.getValue().isDisabled(), e.getValue().getFormat()))
            .filter(c -> !c.disabled())
            .map(c -> {
                var channelDefault = Registry.CHANNEL_HANDLERS.get(c.id());
                var format = c.format();

                var messageFormatter = createMessageFormatter(format);

                return new Channel(channelDefault.getId(), channelDefault.getPrefixSelector(), messageFormatter,
                                channelDefault.getRecipientsFilter(), channelDefault.getSenderFilter());
            })
            .forEach(ch -> channels.put(ch.id(), ch));

		channels.trim();
		return channels;
	}

	private static MessageFormatter createMessageFormatter(String format) {
		return new MessageFormatter(format, extractNecessaryPlaceholders(format));
	}

	private static List<PlaceholderHandler> extractNecessaryPlaceholders(String format) {
		return Registry.FORMAT_HANDLERS.stream()
		                               .filter(ph -> ph.shouldApplyFormat(format))
		                               .toList();
	}

	protected List<JsonSerializerRegistration<?>> getSerializers() {
		return List.of(CommonSerializers.identifier());
	}

	private record ChannelStruct(
		Identifier id,
		boolean disabled,
		String format
	){}
}
