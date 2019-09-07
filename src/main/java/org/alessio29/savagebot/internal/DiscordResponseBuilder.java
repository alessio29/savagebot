package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class DiscordResponseBuilder extends ResponseBuilder {
    private final User user;
    private final MessageChannel channel;

    public DiscordResponseBuilder(User user, MessageChannel channel) {
        this.user = user;
        this.channel = channel;
    }

    public void sendResponse() {
        String privatePart = this.privatePart.toString();
        String publicPart = this.publicPart.toString();
        if (publicPart.length() > 0 && hasCommandResult) {
            Messages.sendMessage(user, channel, publicPart, false);
        }
        if (privatePart.length() > 0) {
            Messages.sendMessage(user, channel, privatePart, true);
        }
    }

    public void reportError(String word, Exception e) {
        Messages.sendMessage(user, channel, "Error while executing command " + word + ". Details: " + e.getMessage(), false);
    }
}
