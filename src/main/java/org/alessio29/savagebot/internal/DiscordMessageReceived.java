package org.alessio29.savagebot.internal;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DiscordMessageReceived implements IMessageReceived<MessageReceivedEvent> {
    private final MessageReceivedEvent event;

    private final String guildId;
    private final String channelId;
    private final String authorId;
    private final String authorMention;
    private final String rawMessage;
    private final List<String> mentions;

    public DiscordMessageReceived(MessageReceivedEvent event) {
        this.event = event;
        if (event.getMessage().getChannelType() != ChannelType.TEXT ) {
            this.guildId = null;
        } else {
            this.guildId = event.getGuild().getId();
        }
        this.channelId = event.getChannel().getId();
        this.authorId = event.getAuthor().getId();
        this.authorMention = event.getAuthor().getAsMention();
        this.rawMessage = event.getMessage().getContentRaw();
        if (event.isFromGuild()) {
            this.mentions = event.getMessage().getMentions().getUsers().stream()
                    .map(IMentionable::getAsMention).collect(Collectors.toList());
        } else {
            this.mentions = Collections.emptyList();
        }
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

    public List<String> getMentions() {
        return mentions;
    }
}
