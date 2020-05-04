package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class TakeBenniesAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("Command syntax: use <CharName>", args.length + 1);
        }
        String charName = ReplyBuilder.createNameFromArgs(args, 0);
        Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), charName);
        if (character !=null) {
            ReplyBuilder replyBuilder = new ReplyBuilder();
            if (character.getBennies()>0) {
                character.useBenny();
                replyBuilder.attach(charName).attach(" used benny.").newLine();
            } else {
                replyBuilder.attach("Character ").attach(charName).attach(" has no bennies.");
            }
            return new CommandExecutionResult(replyBuilder.toString(), args.length + 1);
        }
        return new CommandExecutionResult(charName + " not found.", args.length + 1);
    }
}
