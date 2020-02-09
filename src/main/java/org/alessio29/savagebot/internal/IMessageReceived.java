package org.alessio29.savagebot.internal;

import java.util.List;

public interface IMessageReceived<E> {

    boolean isPrivateMessage();

    String getGuildId();

    String getChannelId();

    String getAuthorId();

    String getAuthorMention();

//    List<String> getMentions();

    String getRawMessage();

    E getOriginalEvent();

}
