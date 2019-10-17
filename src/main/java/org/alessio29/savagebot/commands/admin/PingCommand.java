package org.alessio29.savagebot.commands.admin;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.commands.CommandCategory;
import org.alessio29.savagebot.internal.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;

public class PingCommand implements ICommand {

    public PingCommand() {
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.ADMIN;
    }

    @Override
    public String getDescription() {

        return "Checks SavageBot readiness";
    }

    @Override
    public String[] getArguments() {
        return null;
    }

    @Override
    public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
        return new CommandExecutionResult(
                new ReplyBuilder().attach("Hey, ").
                        attach(ReplyBuilder.mention(event.getAuthor())).
                        attach(" SavageBot is ready!").newLine().toString(),
                1);
    }

}
