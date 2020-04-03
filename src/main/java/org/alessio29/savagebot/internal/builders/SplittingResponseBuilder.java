package org.alessio29.savagebot.internal.builders;

import java.util.List;
import java.util.UUID;

import static org.alessio29.savagebot.internal.builders.MessageSplitter.splitMessage;

public abstract class SplittingResponseBuilder extends ResponseBuilder {

    private final int messageLengthLimit;

    @SuppressWarnings("ConstantConditions")
    private static final int RESERVED_SUFFIX_LENGTH =
            Math.max(ReplyBuilder.SPACE.length(), ReplyBuilder.NEWLINE.length());

    public SplittingResponseBuilder(int messageLengthLimit) {
        this.messageLengthLimit = messageLengthLimit;
    }

    public void sendResponse() {
        String privatePart = this.privatePart.toString();
        String publicPart = this.publicPart.toString();
        if (publicPart.length() > 0 && hasCommandResult) {
            splitAndSendToOrigin(publicPart);
        }
        if (privatePart.length() > 0) {
            splitAndSendPrivate(privatePart);
        }
    }

    public void reportError(UUID id, String word, Exception e) {
        splitAndSendToOrigin("Error while executing command " + word + ". Details: " + e.getMessage()+"\n["+id.toString()+"]");
    }

    private void splitAndSendToOrigin(String message) {
        String asMention = getUserMention();
        int reservedHeaderLength = asMention.length() + RESERVED_SUFFIX_LENGTH;

        List<String> messageParts = splitMessage(message, messageLengthLimit - reservedHeaderLength);

        String header;
        if (message.contains("\n") || messageParts.size() > 1) {
            header = asMention + ReplyBuilder.NEWLINE;
        } else {
            header = asMention + ReplyBuilder.SPACE;
        }

        for (String part : messageParts) {
            sendReplyToOrigin(header + part);
        }
    }

    private void splitAndSendPrivate(String message) {
        List<String> messageParts = splitMessage(message, messageLengthLimit);
        for (String part : messageParts) {
            sendPrivateReply(part);
        }
    }

}
