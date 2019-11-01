package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.commands.CommandCallback;
import org.alessio29.savagebot.commands.CommandCategory;
import org.alessio29.savagebot.commands.CommandCategoryOwner;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.Set;

@CommandCategoryOwner(CommandCategory.TOKENS)
public class TokenCommands {

    private static final int NAME_SIZE = 15;
    private static final int TOKEN_SIZE = 5;

    @CommandCallback(
            name = "clear",
            description = "Clear tokens for character/all characters",
            aliases = {},
            arguments = {"<character_name>/all"}
    )
    public static CommandExecutionResult clear(MessageReceivedEvent event, String[] args) {

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

    @CommandCallback(
            name = "tokens",
            description = "List all characters and their tokens",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult list(MessageReceivedEvent event, String[] args) {
        Guild guild = event.getGuild();
        Channel channel = event.getTextChannel();
        Set<Character> chars = Characters.getCharacters(guild, channel);
        if (chars.isEmpty()) {
            return new CommandExecutionResult("No characters with tokens defined!",1);
        }
        ReplyBuilder replyBuilder = new ReplyBuilder();
        replyBuilder.newLine().
                blockQuote().
                rightPad("NAME", NAME_SIZE).
                tab().
                rightPad("TOKENS", TOKEN_SIZE).
                newLine();
        for (Character chr : chars ) {
            replyBuilder.rightPad(chr.getName(), NAME_SIZE).
                    tab().
                    rightPad(String.valueOf(chr.getTokens()), TOKEN_SIZE).
                    newLine();
        }
        replyBuilder.blockQuote();
        return new CommandExecutionResult(replyBuilder.toString(), 2);
    }

    @CommandCallback(
            name = "take",
            description = "Take token(s) from character",
            aliases = {},
            arguments = {"<character_name>", "[<amount of tokens>]"}
    )
    public static CommandExecutionResult take(MessageReceivedEvent event, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }
        int tokens = 1;
        if (args.length > 1) {
            try {
                tokens = Integer.parseInt(args[1]);
            } catch (Exception ignored) {
            }
        }
        Guild guild = event.getGuild();
        Channel channel = event.getTextChannel();
        Character character = Characters.getCharacterByName(guild, channel, args[0]);
        if (character == null) {
            return new CommandExecutionResult("Cannot find character named " + args[0], 1);
        }
        if (tokens > 0) {
            character.removeTokens(tokens);
            Characters.storeCharacter(guild, channel, character);
            return new CommandExecutionResult(tokens + " token(s) taken from character " + character.getName(), args.length+1);
        }
        return new CommandExecutionResult("Only positive values can be added!", 2);
    }

    @CommandCallback(
            name = "give",
            description = "Give token(s) to character",
            aliases = {},
            arguments = {"<character_name>", "[<amount of tokens>]"}
    )
    public static CommandExecutionResult give(MessageReceivedEvent event, String[] args) {

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
