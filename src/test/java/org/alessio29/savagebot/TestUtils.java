package org.alessio29.savagebot;

import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.DiscordResponseBuilder;
import org.alessio29.savagebot.internal.builders.SplittingResponseBuilder;
import org.alessio29.savagebot.internal.commands.CommandInterpreter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TestUtils {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    public static String normalize(String string) {
        if (!LINE_SEPARATOR.equals("\n")) {
            string = string.replace(LINE_SEPARATOR, "\n");
        }
        return string.trim();
    }

    public static class Message implements IMessageReceived<Message> {
        private final String guildId;
        private final String channelId;
        private final String userId;
        private final String message;
        private final boolean isPrivate;

        public Message(String guildId, String channelId, String userId, String message, boolean isPrivate) {
            this.guildId = guildId;
            this.channelId = channelId;
            this.userId = userId;
            this.message = message;
            this.isPrivate = isPrivate;
        }

        @Override
        public boolean isPrivateMessage() {
            return isPrivate;
        }

        @Override
        public String getGuildId() {
            return guildId;
        }

        @Override
        public String getChannelId() {
            return channelId;
        }

        @Override
        public String getAuthorId() {
            return userId;
        }

        @Override
        public String getAuthorMention() {
            return "@" + userId;
        }

        @Override
        public String getRawMessage() {
            return message;
        }

        @Override
        public Message getOriginalEvent() {
            return this;
        }

        @Override
        public String toString() {
            return "[" +
                    "guildId='" + guildId + '\'' +
                    ", channelId='" + channelId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", isPrivate=" + isPrivate +
                    "] '" + message + '\'';
        }
    }

    public static class MessageBuilder {
        private String guildId;
        private String channelId;
        private String userId;

        public MessageBuilder setGuildId(String guildId) {
            this.guildId = guildId;
            return this;
        }

        public MessageBuilder setChannelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public MessageBuilder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Message message(String message) {
            return new Message(guildId, channelId, userId, message, false);
        }

        public Message privateMessage(String message) {
            return new Message(guildId, channelId, userId, message, true);
        }
    }

    public static class StringResponseBuilder extends SplittingResponseBuilder {
        private final StringBuilder stringBuilder;
        private final IMessageReceived context;

        public StringResponseBuilder(
                int messageLengthLimit,
                StringBuilder stringBuilder,
                IMessageReceived context
        ) {
            super(messageLengthLimit);
            this.stringBuilder = stringBuilder;
            this.context = context;
        }

        @Override
        protected String getUserMention() {
            return context.getAuthorMention();
        }

        @Override
        protected void sendReplyToOrigin(String message) {
            stringBuilder.append(">> [")
                    .append("guildId: ").append(context.getGuildId()).append("; ")
                    .append("channelId: ").append(context.getChannelId()).append("; ")
                    .append("userId: ").append(context.getAuthorId())
                    .append("]\n")
                    .append(message)
                    .append("\n");
        }

        @Override
        protected void sendPrivateReply(String message) {
            stringBuilder.append(">> [private: ")
                    .append("userId: ").append(context.getAuthorId())
                    .append("]\n")
                    .append(message)
                    .append("\n");
        }
    }

    public static MessageBuilder createDefaultForTests() {
        return new MessageBuilder()
                .setGuildId("test-guild").setChannelId("test-channel").setUserId("test-user");
    }

    public static String processMessages(Message... messages) {
        StringBuilder result = new StringBuilder();

        for (Message message : messages) {
            result.append("<< ").append(message).append('\n');
            StringResponseBuilder responseBuilder =
                    new StringResponseBuilder(DiscordResponseBuilder.MESSAGE_LENGTH_LIMIT, result, message);
            new CommandInterpreter().run(message, responseBuilder);
            responseBuilder.sendResponse();
        }

        return result.toString().trim();
    }
}
