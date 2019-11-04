package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.diceRolls.ParseAndRollAction;
import org.alessio29.savagebot.apiActions.diceRolls.RollDiceAction;
import org.alessio29.savagebot.apiActions.diceRolls.RollHistogramAction;
import org.alessio29.savagebot.apiActions.diceRolls.RollSortedAction;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

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
        return new RollDiceAction().doAction(event, args);
    }

    @ParsingCommandCallback
    public static CommandExecutionResult parseAndRollDice(MessageReceivedEvent event, String command) {
        return new ParseAndRollAction().doAction(event, new String[]{command});
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
        return new RollSortedAction().doAction(event, args);
    }

    @CommandCallback(
            name = "rh",
            description = "rolls multiple dice and prints a distribution of results.\n" +
                    "Example: `!rh 1000x2d6`",
            aliases = {},
            arguments = { "<expression_1> ... <expressionN>" }
    )
    public static CommandExecutionResult rollHistogram(MessageReceivedEvent event, String[] args) {
        return new RollHistogramAction().doAction(event, args);
    }
}
