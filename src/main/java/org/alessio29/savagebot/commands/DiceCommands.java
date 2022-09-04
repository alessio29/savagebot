package org.alessio29.savagebot.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.alessio29.savagebot.apiActions.diceRolls.*;
import org.alessio29.savagebot.internal.IMessageReceived;
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
    @DiscordCommandCallback(
            name = "roll",
            description = "roll dice",
            options = {
                    @DiscordOption(name = "roll", description = "dice to roll, might be mixed with text, e.g.: 'damage 2d8'", isRequired = true)
            }
    )
    public static CommandExecutionResult rollDice(IMessageReceived message, String[] args) {
        return new RollDiceAction().doAction(message, args);
    }

    @ParsingCommandCallback
    public static CommandExecutionResult parseAndRollDice(IMessageReceived message, String command) {
        return new ParseAndRollAction().doAction(message, new String[]{command});
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
    public static CommandExecutionResult rollSorted(IMessageReceived message, String[] args) {

        return new RollSortedAction().doAction(message, args);
    }

    @CommandCallback(
            name = "rh",
            description = "rolls multiple dice and prints a distribution of results.\n" +
                    "Example: `!rh 1000x2d6`",
            aliases = {},
            arguments = { "<expression_1> ... <expressionN>" }
    )
    public static CommandExecutionResult rollHistogram(IMessageReceived message, String[] args) {
        return new RollHistogramAction().doAction(message, args);
    }

    @CommandCallback(
            name = "ept",
            description = "rolls HP by Empire of Petal Throne rules.\n" +
                    "Example: `!ept 4 d6+1`",
            aliases = {},
            arguments = { "<level> <hit_die_expression>"}
    )
    public static CommandExecutionResult rollEmpireOfPetalThroneHP(IMessageReceived message, String[] args) {
        return new RollEmpireOfPetalThroneHPAction().doAction(message, args);
    }
}
