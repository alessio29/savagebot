package org.alessio29.savagebot.internal;

import java.util.List;

public interface IMessageReceived<E> {

    String getGuildId();

    String getChannelId();

    String getAuthorId();

    String getAuthorMention();

    String getRawMessage();

    E getOriginalEvent();

    List<String> getMentions();

}
