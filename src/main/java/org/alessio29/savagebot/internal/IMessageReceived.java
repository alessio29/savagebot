package org.alessio29.savagebot.internal;

public interface IMessageReceived<E> {

    String getGuildId();

    String getChannelId();

    String getAuthorId();

    String getAuthorMention();

    String getRawMessage();

    E getOriginalEvent();

}
