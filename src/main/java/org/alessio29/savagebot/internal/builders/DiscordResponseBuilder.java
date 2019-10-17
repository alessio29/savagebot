package org.alessio29.savagebot.internal.builders;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DiscordResponseBuilder extends ResponseBuilder {

    private final static int MESSAGE_LENGTH_LIMIT = 2000;
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
            sendMessage(user, channel, publicPart, false);
        }
        if (privatePart.length() > 0) {
            sendMessage(user, channel, privatePart, true);
        }
    }

    public void reportError(String word, Exception e) {
        sendMessage(user, channel, "Error while executing command " +
                word + ". Details: " + e.getMessage(), false);
    }

    private static void sendMessage(User user, MessageChannel messageChannel,
                                    String message, boolean isPrivate) {
        List<String> messageParts = splitMessage(message);
        if (isPrivate) {
            for (String part : messageParts) {
                user.openPrivateChannel().queue((channel) ->
                        channel.sendMessage(part).queue());
            }
        } else {
            for (String part : messageParts) {
                messageChannel.sendMessage(user.getAsMention()+
                        ReplyBuilder.SPACE+part).queue();
            }
        }
    }

    private static List<String> splitMessage(String message) {
        List<String> result = new ArrayList<>();
        if (message.length()<MESSAGE_LENGTH_LIMIT) {
            return Collections.singletonList(message);
        }
        String[] lines = message.split(ReplyBuilder.NEWLINE);
        for (String line : lines) {
            if (line.length() > MESSAGE_LENGTH_LIMIT) {
                result.addAll(splitLine(line));
            } else {
                result.add(line);
            }
            result.add(ReplyBuilder.NEWLINE);
        }

        return result;
    }

    private static Collection<? extends String> splitLine(String line) {

        if (line.length()<MESSAGE_LENGTH_LIMIT) {
            return Collections.singletonList(line);
        }
        List<String> result = new ArrayList<>();
        while (line.length() > MESSAGE_LENGTH_LIMIT) {
            result.add(line.substring(0, MESSAGE_LENGTH_LIMIT));
            line = line.substring(0, MESSAGE_LENGTH_LIMIT);
        }
        result.add(line);
        return result;
    }
}