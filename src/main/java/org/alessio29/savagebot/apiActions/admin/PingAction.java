package org.alessio29.savagebot.apiActions.admin;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class PingAction implements IDiscordAction {
    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        return new CommandExecutionResult(
                new ReplyBuilder().attach("Hey, ").
                        attach(ReplyBuilder.mention(event.getAuthor())).
                        attach(" SavageBot is ready!").newLine().toString(),
                1);
    }
}
