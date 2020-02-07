package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.states.AddStatesAction;
import org.alessio29.savagebot.apiActions.states.ClearStatesAction;
import org.alessio29.savagebot.apiActions.states.ListStatesAction;
import org.alessio29.savagebot.apiActions.states.RemoveStateAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.STATES)
public class StatesCommands {

    @CommandCallback(
            name = "clstate",
            description = "Clear states for character/all characters",
            aliases = {"cs"},
            arguments = {"<character_name>/all"}
    )
    public static CommandExecutionResult clear(IMessageReceived message, String[] args) {

        return new ClearStatesAction().doAction(message, args);
    }

    @CommandCallback(
            name = "state",
            description = "Set states for character/all characters",
            aliases = {"st"},
            arguments = {"<character_name> <state1> [<state2>] [...]"}
    )
    public static CommandExecutionResult add(IMessageReceived message, String[] args) {

        return new AddStatesAction().doAction(message, args);
    }

    @CommandCallback(
            name = "remstate",
            description = "Remove states from character",
            aliases = {"rst"},
            arguments = {"<character_name> <state1> [<state2>] [...]"}
    )
    public static CommandExecutionResult remove(IMessageReceived message, String[] args) {

        return new RemoveStateAction().doAction(message, args);
    }

    @CommandCallback(
            name = "list",
            description = "List characters with states",
            aliases = {""},
            arguments = {""}
    )
    public static CommandExecutionResult list(IMessageReceived message, String[] args) {

        return new ListStatesAction().doAction(message, args);
    }

}
