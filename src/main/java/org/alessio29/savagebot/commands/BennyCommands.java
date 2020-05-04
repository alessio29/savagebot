package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.bennies.ClearBenniesAction;
import org.alessio29.savagebot.apiActions.bennies.GiveBenniesAction;
import org.alessio29.savagebot.apiActions.bennies.TakeBenniesAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.BENNIES)
public class BennyCommands {

    @CommandCallback(
            name = "takebenny",
            description = "Use character benny",
            aliases = {"tb"},
            arguments = {"<character_name>"}
    )
    public static CommandExecutionResult take(IMessageReceived message, String[] args) {
        return new TakeBenniesAction().doAction(message, args);
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
}
