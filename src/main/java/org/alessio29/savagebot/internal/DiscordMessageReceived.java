package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DiscordMessageReceived implements IMessageReceived<MessageReceivedEvent> {
    private final MessageReceivedEvent event;

    public DiscordMessageReceived(MessageReceivedEvent event) {
        this.event = event;
    }

    @Override
    public boolean isPrivateMessage() {
        return event.getChannelType() == ChannelType.PRIVATE;
    }

    @Override
    public String getGuildId() {
        return event.getGuild().getId();
    }

    @Override
    public String getChannelId() {
        return event.getChannel().getId();
    }

    @Override
    public String getAuthorId() {
        return event.getAuthor().getId();
    }

    @Override
    public String getAuthorMention() {
        return event.getAuthor().getAsMention();
    }

    @Override
    public String getRawMessage() {
        return event.getMessage().getContentRaw();
    }

    @Override
    public MessageReceivedEvent getOriginalEvent() {
        return event;
    }
}
