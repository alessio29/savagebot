package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.bennies.*;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.BENNIES)
public class BennyCommands {

    @CommandCallback(
            name = "takebenny",
            description = "Use character benny",
            aliases = {"tb"},
            arguments = {"<character_name> [<bennyColor>]"}
    )
    public static CommandExecutionResult take(IMessageReceived message, String[] args) {
        return new TakeBenniesAction().doAction(message, args);
    }

    @CommandCallback(
            name = "setbennymode",
            description = "Set benny mode",
            aliases = {"sbm"},
            arguments = {"normal/deadlands"}
    )
    public static CommandExecutionResult setBennyMode(IMessageReceived message, String[] args) {
        return new SetBennyModeAction().doAction(message, args);
    }

    @CommandCallback(
            name = "addbenny",
            description = "Add benny to pool",
            aliases = {"ab"},
            arguments = {"w/b/r/g"}
    )
    public static CommandExecutionResult addBenny(IMessageReceived message, String[] args) {
        return new AddBennyAction().doAction(message, args);
    }

    @CommandCallback(
            name = "clearbennies",
            description = "clear character(s) benny",
            aliases = {"cb"},
            arguments = {"<character1_name>/all [<character2_name>]"}
    )
    public static CommandExecutionResult clear(IMessageReceived message, String[] args) {
        return new ClearBenniesAction().doAction(message, args);
    }

    @CommandCallback(
            name = "givebenny",
            description = "Give new benny(ies) to character",
            aliases = {"gb"},
            arguments = {"<character_name>", "[<amount>]"}
    )
    public static CommandExecutionResult give(IMessageReceived message, String[] args) {
        return new GiveBenniesAction().doAction(message, args);
    }

    @CommandCallback(
            name = "pullbenny",
            description = "Pull benny for character from bennies pool",
            aliases = {"pb"},
            arguments = {"<character_name> [<amount>]"}
    )
    public static CommandExecutionResult pull(IMessageReceived message, String[] args) {
        return new PullBenniesAction().doAction(message, args);
    }

    @CommandCallback(
            name = "initbennies",
            description = "Create initial bennies pool",
            aliases = {"ib"},
            arguments = {}
    )
    public static CommandExecutionResult init(IMessageReceived message, String[] args) {
        return new InitBenniesAction().doAction(message, args);
    }
}
