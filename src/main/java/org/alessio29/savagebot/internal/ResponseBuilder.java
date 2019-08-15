package org.alessio29.savagebot.internal;

public abstract class ResponseBuilder {
    protected boolean hasCommandResult = false;
    protected final StringBuilder publicPart = new StringBuilder();
    protected final StringBuilder privatePart = new StringBuilder();

    public void addRaw(String string) {
        publicPart.append(string).append(" ");
    }

    public void addResult(CommandExecutionResult result) {
        if (result.isPrivateMessage()) {
            privatePart.append(result.getResult()).append(" ");
        } else  {
            hasCommandResult = true;
            publicPart.append(result.getResult()).append(" ");
        }
    }

    public abstract void reportError(String word, Exception e);
}
