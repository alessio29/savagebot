package org.alessio29.savagebot.apiActions.tokens;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.Set;

public class ClearTokensAction implements IDiscordAction {
    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        if (args.length <1) {
            return new CommandExecutionResult("Please provide character name or 'all' to clear all characters", 1);
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
