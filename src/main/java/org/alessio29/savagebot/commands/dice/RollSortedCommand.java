package org.alessio29.savagebot.commands.dice;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.commands.CommandCategory;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.commands.ICommand;
import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.RollSortedInterpreter;
import org.alessio29.savagebot.r2.parse.Parser;

public class RollSortedCommand implements ICommand {
    @Override
    public String getName() {
        return "rs";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DICE;
    }

    @Override
    public String getDescription() {
        return "rolls multiple dice and print them out sorted.\n" +
                "This is mostly useful for rolling initiative as a single command.\n" +
                "`!rs Huey d20 Dewey d20 Louie d20` => \n" +
                "```\n" +
                "Dewey 14\n" +
                "Huey 10\n" +
                "Louie 5\n" +
                "```";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "roll_sorted", "roll-sorted", "rollsorted" };
    }

    @Override
    public String[] getArguments() {
        return new String[] { "[<heading_1>] <expression_1> ... [<heading_N>] <expression_N>" };
    }

    @Override
    public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("No commands", args.length + 1);
        }

        String result = new RollSortedInterpreter(new CommandContext()).run(new Parser().parse(args));

        return new CommandExecutionResult(result, args.length + 1);
    }
}
