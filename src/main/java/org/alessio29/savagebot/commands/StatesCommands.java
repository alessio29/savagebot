package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.states.ManageStatesAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.STATES)
public class StatesCommands {

    @CommandCallback(
            name = "state",
            description = "Manage states for characters ",
            aliases = {"st"},
            arguments = {"<character_name> [clear] [+/-]<state1> [<state2>] [...]"}
    )
    public static CommandExecutionResult add(IMessageReceived message, String[] args) {

        return new ManageStatesAction().doAction(message, args);
    }
}
