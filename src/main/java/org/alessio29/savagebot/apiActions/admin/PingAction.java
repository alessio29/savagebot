package org.alessio29.savagebot.apiActions.admin;

import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class PingAction {

    public CommandExecutionResult doAction(String userMention) {
        return new CommandExecutionResult(
                new ReplyBuilder().attach("Hey, ").
                        attach(userMention).
                        attach(" SavageBot is ready!").newLine().toString(),
                1);
    }
}
