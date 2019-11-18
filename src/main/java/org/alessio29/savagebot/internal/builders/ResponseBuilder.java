package org.alessio29.savagebot.internal.builders;

import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public abstract class ResponseBuilder {
    protected boolean hasCommandResult = false;
    protected final StringBuilder publicPart = new StringBuilder();
    protected final StringBuilder privatePart = new StringBuilder();

    public void addRaw(String string) {
        publicPart.append(string);
        if (shouldAppendWhitespace(string)) {
            publicPart.append(' ');
        }
    }

    public void addResult(CommandExecutionResult result) {
        String resultString = result.getResult();
        String toAppend = shouldAppendWhitespace(resultString) ? resultString + " " : resultString;
        if (result.isPrivateMessage()) {
            privatePart.append(toAppend);
        } else  {
            hasCommandResult = true;
            publicPart.append(toAppend);
        }
    }

    private static boolean shouldAppendWhitespace(String string) {
        if (string.length() <= 0) return false;
        char last = string.charAt(string.length() - 1);
        return !Character.isWhitespace(last);
    }

    public abstract void reportError(String word, Exception e);

    protected abstract String getUserMention();
    protected abstract void sendReplyToOrigin(String message);
    protected abstract void sendPrivateReply(String message);
}
