package org.alessio29.savagebot.internal.builders;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DiscordResponseBuilder extends ResponseBuilder {

    private final static int MESSAGE_LENGTH_LIMIT = 2000;

    @SuppressWarnings("ConstantConditions")
    private static final int RESERVED_SUFFIX_LENGTH =
            Math.max(ReplyBuilder.SPACE.length(), ReplyBuilder.NEWLINE.length());

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
            sendMessage(user, channel, publicPart);
        }
        if (privatePart.length() > 0) {
            sendPrivateMessage(user, privatePart);
        }
    }

    public void reportError(String word, Exception e) {
        sendMessage(user, channel, "Error while executing command " + word + ". Details: " + e.getMessage());
    }

    private static void sendMessage(
            User user,
            MessageChannel messageChannel,
            String message
    ) {
        String asMention = user.getAsMention();
        int reservedHeaderLength = asMention.length() + RESERVED_SUFFIX_LENGTH;

        List<String> messageParts = splitMessage(message, MESSAGE_LENGTH_LIMIT - reservedHeaderLength);

        String header;
        if (message.contains("\n") || messageParts.size() > 1) {
            header = asMention + ReplyBuilder.NEWLINE;
        } else {
            header = asMention + ReplyBuilder.SPACE;
        }

        for (String part : messageParts) {
            messageChannel.sendMessage(header + part).queue();
        }
    }

    private static void sendPrivateMessage(User user, String message) {
        List<String> messageParts = splitMessage(message, MESSAGE_LENGTH_LIMIT);
        for (String part : messageParts) {
            user.openPrivateChannel().queue(channel -> channel.sendMessage(part).queue());
        }
    }

    public static List<String> splitMessage(String message, int partLengthLimit) {
        if (message.length() < partLengthLimit) {
            return Collections.singletonList(message);
        }

        List<String> result = new ArrayList<>();
        StringBuilder partBuilder = new StringBuilder();

        for (String line : message.split(ReplyBuilder.NEWLINE)) {
            line = line.trim();

            if (line.length() > partLengthLimit) {
                if (partBuilder.length() > 0) {
                    result.add(partBuilder.toString().trim());
                    partBuilder.setLength(0);
                }
                result.addAll(splitLine(line, partLengthLimit));
            } else if (partBuilder.length() + line.length() + 1 > partLengthLimit) {
                // Part length here is a bit overestimated,
                // but that should be fine for Discord message length limit.
                result.add(partBuilder.toString().trim());
                partBuilder.setLength(0);
                partBuilder.append(line).append(ReplyBuilder.NEWLINE);
            } else {
                partBuilder.append(line).append(ReplyBuilder.NEWLINE);
            }
        }

        if (partBuilder.length() > 0) {
            result.add(partBuilder.toString().trim());
        }

        return result;
    }

    private static Collection<? extends String> splitLine(String line, int partLengthLimit) {
        if (line.length() < partLengthLimit) {
            return Collections.singletonList(line);
        }

        List<String> result = new ArrayList<>();
        while (line.length() > partLengthLimit) {
            result.add(line.substring(0, partLengthLimit).trim());
            line = line.substring(partLengthLimit);
        }
        result.add(line);

        return result;
    }
}