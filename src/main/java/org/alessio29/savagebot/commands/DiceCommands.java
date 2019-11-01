package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.commands.CommandCallback;
import org.alessio29.savagebot.commands.CommandCategory;
import org.alessio29.savagebot.commands.CommandCategoryOwner;
import org.alessio29.savagebot.commands.ParsingCommandCallback;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.RollInterpreter;
import org.alessio29.savagebot.r2.eval.RollSortedInterpreter;
import org.alessio29.savagebot.r2.parse.Parser;
import org.alessio29.savagebot.r2.tree.NonParsedStringStatement;
import org.alessio29.savagebot.r2.tree.Statement;

import java.util.List;

@SuppressWarnings("unused")
@CommandCategoryOwner(CommandCategory.DICE)
public class DiceCommands {

    @CommandCallback(
            name = "r",
            description = "rolls dice.\n" +
                    "Multiple die rolls in a single command: `!r 2d6+d4+2 d12 d6!`\n" +
                    "Repeated die rolls: `!r 6x4d6k3`\n" +
                    "Inline comments: `!r shooting s8 damage 2d6+1`" +
                    "Just roll them bones (no spaces in expressions): `shooting !s8 damage !2d6+1`",
            aliases = {},
            arguments = { "<expression1> ... <expressionN> "}
    )
    public static CommandExecutionResult rollDice(MessageReceivedEvent event, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("No commands", args.length + 1);
        }

        String result = new RollInterpreter(new CommandContext()).run(new Parser().parse(args));

        return new CommandExecutionResult(result, args.length + 1);
    }

    @ParsingCommandCallback
    public static CommandExecutionResult parseAndRollDice(MessageReceivedEvent event, String command) {
        List<Statement> statements = new Parser().parseCommandElement(command);
        if (statements.stream().allMatch(statement -> statement instanceof NonParsedStringStatement)) {
            return null;
        }

        String result = new RollInterpreter(new CommandContext()).run(statements);

        return new CommandExecutionResult(result, 1);
    }

    @CommandCallback(
            name = "rs",
            description = "rolls multiple dice and print them out sorted.\n" +
                    "This is mostly useful for rolling initiative as a single command.\n" +
                    "`!rs Huey d20 Dewey d20 Louie d20` => \n" +
                    "```\n" +
                    "Dewey 14\n" +
                    "Huey 10\n" +
                    "Louie 5\n" +
                    "```",
            aliases = {},
            arguments = { "[<heading_1>] <expression_1> ... [<heading_N>] <expression_N>" }
    )
    public static CommandExecutionResult rollSorted(MessageReceivedEvent event, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("No commands", args.length + 1);
        }

        String result = new RollSortedInterpreter(new CommandContext()).run(new Parser().parse(args));

        return new CommandExecutionResult(result, args.length + 1);
    }

}
