package org.alessio29.savagebot.internal.builders;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.characters.CharacterInitiative;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReplyBuilder {

    static final String NEWLINE = "\n";
    static final String SPACE = " ";
    private static final String BLOCK_MARKER = "```";
    private static final String QUOTE_MARKER = ">";
    private static final String SQUARE_BRACKET_OPEN = "[";
    private static final String SQUARE_BRACKET_CLOSE = "]";
    private static final String TAB = "\t";

    private StringBuilder builder;

    public ReplyBuilder() {
        this.builder = new StringBuilder();
    }

    public static String removeBlocks(String rawMessage) {

        while (rawMessage.contains(BLOCK_MARKER)) {
            int blockStarts = rawMessage.indexOf(BLOCK_MARKER);
            int blockStops = rawMessage.indexOf(BLOCK_MARKER, blockStarts + 1);
            if (blockStops == -1) {
                return rawMessage;
            }
            rawMessage = rawMessage.substring(0, blockStarts) + rawMessage.substring(blockStops + 3);
        }
        return rawMessage;
    }

    public static String removeQuotes(String rawMessage) {

        String[] lines = rawMessage.split(NEWLINE);
        ArrayList<String> res = new ArrayList<>();

        for (String line : lines) {
            if (!line.startsWith(QUOTE_MARKER)) {
                res.add(line);
            }
        }
        return String.join(NEWLINE, res);
    }

    public static String mention(User user) {
        return user.getAsMention();
    }

    public static String bold(String message) {
        return "**" + message + "**";
    }

    public static String italic(String message) {
        return "*" + message + "*";
    }

    public static String underlined(String message) {
        return "__" + message + "__";
    }

    public static String strikeout(String message) {
        return "~~" + message + "~~";
    }

    public static String capitalize(String charName) {
        return charName.substring(0, 1).toUpperCase() + charName.substring(1);
    }

    public static List<String> bold(String[] list) {

        List<String> res = new ArrayList<>();
        for (String s : list) {
            res.add(bold(s));
        }
        return res;
    }

    public static String createNameFromArgs(String[] args, int startFrom) {
        ReplyBuilder charName = new ReplyBuilder();
        for (int i = startFrom; i < args.length; i++) {
            charName.space().attach(args[i]);
        }
        return charName.toString().trim();
    }

    public static String showOrder(MessageReceivedEvent event) {

        ReplyBuilder reply = new ReplyBuilder();
        reply.newLine();

        Set<CharacterInitiative> chars = CharacterInitCache.getCharacters(event.getGuild());
        if (!chars.isEmpty()) {
            for (CharacterInitiative c : CharacterInitCache.getCharacters(event.getGuild())) {
                String allCards = c.getAllCards().stream().map(Card::toString).collect(Collectors.joining(","));
                String paramString = (c.getParams().trim().isEmpty()) ? " - " :
                        new ReplyBuilder().squareBracketOpen().attach(c.getParams()).squareBracketClose().toString();
                reply.attach(c.getName()).
                        attach(paramString).
                        attach(c.getBestCard().toString()).
                        squareBracketOpen().
                        attach(allCards).
                        squareBracketClose().
                        newLine();
            }
        } else {
            reply.attach("No cards dealt!");
        }
        return reply.toString();
    }

    public ReplyBuilder tab() {
        builder.append(TAB);
        return this;
    }

    public ReplyBuilder newLine() {
        builder.append(NEWLINE);
        return this;
    }

    public ReplyBuilder rightPad(String str, int size) {
        builder.append(StringUtils.rightPad(str, size));
        return this;
    }

    public ReplyBuilder attach(String str) {
        builder.append(str);
        return this;
    }

    public String toString() {
        return builder.toString();
    }

    public ReplyBuilder blockQoute() {

        builder.append(BLOCK_MARKER);
        return this;
    }

    public ReplyBuilder space() {
        this.builder.append(SPACE);
        return this;
    }

    private ReplyBuilder squareBracketOpen() {
        this.builder.append(SQUARE_BRACKET_OPEN);
        return this;
    }

    private ReplyBuilder squareBracketClose() {
        this.builder.append(SQUARE_BRACKET_CLOSE);
        return this;
    }
}