package org.alessio29.savagebot.internal.builders;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class DiscordResponseBuilder extends SplittingResponseBuilder {
    public final static int MESSAGE_LENGTH_LIMIT = 2000;

    public final static int MESSAGE_PARTS_LIMIT = 3;

    private final User user;
    private final MessageChannel channel;

    public DiscordResponseBuilder(User user, MessageChannel channel) {
        super(MESSAGE_LENGTH_LIMIT);
        this.user = user;
        this.channel = channel;
    }

    @Override
    protected void sendReplyPartsToOrigin(List<String> parts) {
        if (parts.size() > MESSAGE_PARTS_LIMIT) {
            super.sendReplyPartsToOrigin(parts.subList(0, MESSAGE_PARTS_LIMIT));
            sendReplyToOrigin(
                    "...and so on. Command result is too long. " +
                    "If you really want to do such thing, you can send commands to bot privately."
            );
            return;
        }
        super.sendReplyPartsToOrigin(parts);
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