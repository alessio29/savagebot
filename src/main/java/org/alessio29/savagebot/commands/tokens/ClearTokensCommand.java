package org.alessio29.savagebot.commands.tokens;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.commands.CommandCategory;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.Set;

public class ClearTokensCommand implements ICommand {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.TOKENS;
    }

    @Override
    public String getDescription() {
        return "Clears tokens for named character or all characters in current channel ";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<character_name>/all"};
    }

    @Override
    public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {

        if (args.length <1) {
            throw new Exception("Please provide character name or 'all' to clear all characters");
        }

        Guild guild = event.getGuild();
        Channel channel = event.getTextChannel();
        Set<Character> chars = Characters.getCharacters(guild, channel);

        String text;
        if (args[0].trim().toLowerCase().equals("all")) {
            chars.clear();
            text = "Removed all characters";
        } else {
            chars.remove(new Character(args[0],0));
            text = "Removed character "+args[0];
        }
        Characters.storeAllCharacters(guild, channel, chars);

        return new CommandExecutionResult(text, args.length+1);
    }
}
