package org.alessio29.savagebot.apiActions.tokens;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class GiveTokenAction implements IDiscordAction {
    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }

        int tokens = 1;
        if (args.length > 1) {
            try {
                tokens = Integer.parseInt(args[1]);
            } catch ( Exception ignored) {
            }
        }
        Guild guild = event.getGuild();
        Channel channel = event.getTextChannel();
        Character character = Characters.getCharacterByName(guild, channel, args[0]);
        if (character == null) {
            character = new Character(args[0], 0);
        }
        if (tokens>0) {
            character.addTokens(tokens);
            Characters.storeCharacter(guild, channel, character);
            return new CommandExecutionResult( tokens+" token(s) given to character "+character.getName() , args.length+1);
        }
        return new CommandExecutionResult("Only positive values can be added!", 2);
    }
}
