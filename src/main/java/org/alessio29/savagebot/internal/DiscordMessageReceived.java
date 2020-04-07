package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DiscordMessageReceived implements IMessageReceived<MessageReceivedEvent> {
    private final MessageReceivedEvent event;

    private final String guildId;
    private final String channelId;
    private final String authorId;
    private final String authorMention;
    private final String rawMessage;


    public DiscordMessageReceived(MessageReceivedEvent event) {
        this.event = event;
        this.guildId = event.getGuild().getId();
        this.channelId = event.getChannel().getId();
        this.authorId = event.getAuthor().getId();
        this.authorMention = event.getAuthor().getAsMention();
        this.rawMessage = event.getMessage().getContentRaw();
    }

    @Override
    public boolean isPrivateMessage() {
        return event.getChannelType() == ChannelType.PRIVATE;
    }

    @Override
    public String getGuildId() {
        return this.guildId;
    }

    @Override
    public String getChannelId() {
        return this.channelId;
    }

    @Override
    public String getAuthorId() {
        return this.authorId;
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
        return this.authorMention;
    }

    @Override
    public String getRawMessage() {
        return rawMessage;
    }

    @Override
    public MessageReceivedEvent getOriginalEvent() {
        return event;
    }
}
