package org.alessio29.savagebot.apiActions.initiative;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.characters.CharacterInitiative;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.Set;
import java.util.stream.Collectors;

public class ShowInitiativeAction implements IDiscordAction {

    private static final int CARDS_SIZE = 8;
    private static final int CHAR_NAME_SIZE = 25;

    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {

        ReplyBuilder reply = new ReplyBuilder();
        reply.blockQuote().newLine();

        Set<CharacterInitiative> chars = CharacterInitCache.getCharacters(event.getGuild());
        if (!chars.isEmpty()) {
            for (CharacterInitiative c : CharacterInitCache.getCharacters(event.getGuild())) {
                String allCards = c.getAllCards().stream().map(Card::toString).collect(Collectors.joining(", "));
                String paramString;
                if (c.getParams().trim().isEmpty()) {
                    paramString = "    ";
                } else {
                    paramString = new ReplyBuilder().
                            squareBracketOpen().
                            rightPad(c.getParams(), 2).
                            squareBracketClose().
                            toString();
                }
                reply.rightPad(c.getName(), CHAR_NAME_SIZE).tab().
                        attach(paramString).tab().
                        leftPad(c.getBestCard().toString(), CARDS_SIZE).tab().
                        squareBracketOpen().
                        attach(allCards).
                        squareBracketClose().
                        newLine();
            }
        } else {
            reply.attach("No cards dealt!");
        }
        reply.newLine().blockQuote();
        return new CommandExecutionResult(reply.toString(), args.length+1);

    }
}
