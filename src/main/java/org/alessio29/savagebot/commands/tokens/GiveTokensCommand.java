package org.alessio29.savagebot.commands.tokens;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.commands.CommandCategory;
import org.alessio29.savagebot.internal.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class GiveTokensCommand implements ICommand {
    @Override
    public String getName() {
        return "give";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.TOKENS;
    }

    @Override
    public String getDescription() {
        return "Gives character token (benny, Fate point etc..)";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<character_name>", "[<amount of tokens>]"};
    }

    @Override
    public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {

        if (args.length < 1) {
            throw new Exception("Character name missing!");
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

        throw new Exception("Only positive values can be added!");
    }
}
