package fr.bendertales.mc.channels.impl.messages;


import fr.bendertales.mc.channels.api.PerRecipientPlaceholderFormatter;


record IndexedRecipientPlaceholderFormatter(String key,
                                            PerRecipientPlaceholderFormatter formatter,
                                            int index,
                                            int length
) {
}
