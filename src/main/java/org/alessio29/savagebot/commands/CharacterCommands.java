package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.characters.ListCharactersAction;
import org.alessio29.savagebot.apiActions.characters.RemoveCharacterAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.CHARACTERS)
public class CharacterCommands {

    @CommandCallback(
            name = "list",
            description = "List characters with states",
            aliases = {""},
            arguments = {""}
    )
    public static CommandExecutionResult list(IMessageReceived message, String[] args) {

        return new ListCharactersAction().doAction(message, args);
    }

    @CommandCallback(
            name = "remove",
            description = "Remove characters totally",
            aliases = {"rm"},
            arguments = {""}
    )
    public static CommandExecutionResult remove(IMessageReceived message, String[] args) {

        return new RemoveCharacterAction().doAction(message, args);
    }
}
