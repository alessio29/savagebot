package org.alessio29.savagebot.commands.tokens;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.commands.Category;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.internal.CommandExecutionResult;
import org.alessio29.savagebot.internal.Messages;
import org.apache.commons.lang.StringUtils;

import java.util.Set;


public class ListTokensCommand implements ICommand{

    private static final int NAME_SIZE = 15;
    private static final int TOKEN_SIZE = 5;

    @Override
    public String getName() {
        return "tokens";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.TOKENS;
    }

    @Override
    public String getDescription() {
        return "List characters and their tokens";
    }

    @Override
    public String[] getArguments() {
        return null;
    }

    @Override
    public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
        Guild guild = event.getGuild();
        Channel channel = event.getTextChannel();
        Set<Character> chars = Characters.getCharacters(guild, channel);
        if (chars.isEmpty()) {
            return new CommandExecutionResult("No characters with tokens defined!",1);
        }
        StringBuilder reply = new StringBuilder();
        reply.append(Messages.NEWLINE).
                append("```").
                append(StringUtils.rightPad("NAME", NAME_SIZE)).
                append(Messages.TAB).
                append(StringUtils.rightPad("TOKENS", TOKEN_SIZE)).
                append(Messages.NEWLINE);
        for (Character chr : chars ) {
            reply.append(StringUtils.rightPad(chr.getName(), NAME_SIZE)).
                    append(Messages.TAB).
                    append(StringUtils.rightPad(String.valueOf(chr.getTokens()), TOKEN_SIZE)).
                    append(Messages.NEWLINE);
        }
        reply.append("```");
        return new CommandExecutionResult(reply.toString(), 2);
    }
}
