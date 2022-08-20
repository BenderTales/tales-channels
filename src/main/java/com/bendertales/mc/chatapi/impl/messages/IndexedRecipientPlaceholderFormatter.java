package com.bendertales.mc.chatapi.impl.messages;

import com.bendertales.mc.chatapi.api.PerRecipientPlaceholderFormatter;


record IndexedRecipientPlaceholderFormatter(String key,
                                            PerRecipientPlaceholderFormatter formatter,
                                            int index,
                                            int length
) {
}
