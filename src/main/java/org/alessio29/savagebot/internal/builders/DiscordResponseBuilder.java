package org.alessio29.savagebot.internal.builders;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class DiscordResponseBuilder extends SplittingResponseBuilder {
    public final static int MESSAGE_LENGTH_LIMIT = 2000;

    private final User user;
    private final MessageChannel channel;

    public DiscordResponseBuilder(User user, MessageChannel channel) {
        super(MESSAGE_LENGTH_LIMIT);
        this.user = user;
        this.channel = channel;
    }

    @Override
    protected void sendReplyToOrigin(String message) {
        channel.sendMessage(message).queue();
    }

    @Override
    protected void sendPrivateReply(String message) {
        user.openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
    }

    @Override
    protected String getUserMention() {
        return user.getAsMention();
    }
}