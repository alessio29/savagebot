package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.RollInterpreter;
import org.alessio29.savagebot.r2.eval.RollAccumulatingInterpreter;
import org.alessio29.savagebot.r2.parse.Parser;
import org.alessio29.savagebot.r2.tree.NonParsedStringStatement;
import org.alessio29.savagebot.r2.tree.Statement;
import org.jetbrains.annotations.Nullable;

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
        List<Statement> statements = tryParseStatements(command);
        if (statements == null) return null;

        String result = new RollInterpreter(new CommandContext()).run(statements);

        return new CommandExecutionResult(result, 1);
    }

    @Nullable
    private static List<Statement> tryParseStatements(String command) {
        Parser parser = new Parser();
        List<Statement> statements = parser.parseCommandElement(command);
        if (hasParsedStatements(statements)) {
            return statements;
        }

        if ((command.startsWith("r") || command.startsWith("R")) && command.length() > 1) {
            statements = parser.parseCommandElement(command.substring(1));
            if (hasParsedStatements(statements)) {
                return statements;
            }
        }

        return null;
    }

    private static boolean hasParsedStatements(List<Statement> statements) {
        return !statements.stream().allMatch(statement -> statement instanceof NonParsedStringStatement);
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

        String result = new RollAccumulatingInterpreter(new CommandContext()).rollSorted(new Parser().parse(args));

        return new CommandExecutionResult(result, args.length + 1);
    }

    @CommandCallback(
            name = "rh",
            description = "rolls multiple dice and prints a distribution of results.\n" +
                    "Example: `!rh 1000x2d6`",
            aliases = {},
            arguments = { "<expression_1> ... <expressionN>" }
    )
    public static CommandExecutionResult rollHistogram(MessageReceivedEvent event, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("No commands", args.length + 1);
        }

        String result = new RollAccumulatingInterpreter(new CommandContext()).rollHistogram(new Parser().parse(args));

        return new CommandExecutionResult(result, args.length + 1);
    }
}
