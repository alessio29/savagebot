package org.alessio29.savagebot.internal;

public class SelfMentionContainer {

    private static String mention = null;

    public static void initialize(String mention) {
        SelfMentionContainer.mention = mention;
    }

    public static String getMention() {
        return SelfMentionContainer.mention;
    }
}
