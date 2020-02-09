package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.stream.Collectors;

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

//    @Override
//    public List<String> getMentions() {
//        return event.getMessage().getMentionedUsers().
//                stream().
//                filter(user -> !user.isBot()).
//                map(user -> user.getId()).
//                collect(Collectors.toList());
//    }

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
