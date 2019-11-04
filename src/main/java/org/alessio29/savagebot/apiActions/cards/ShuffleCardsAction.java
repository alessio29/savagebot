package org.alessio29.savagebot.apiActions.cards;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.List;
import java.util.stream.Collectors;

public class ShuffleCardsAction implements IDiscordAction {
    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());
        deck.shuffle();
        Guild guild = event.getGuild();

        List<User> users = event.getGuild().getMembers().stream().
                filter(m -> m.hasPermission(event.getTextChannel(), Permission.MESSAGE_READ)).
                filter(m->m.getOnlineStatus()== OnlineStatus.ONLINE).map(m -> m.getUser()).collect(Collectors.toList());
        for (User user : users) {
            Hands.getHand(guild, user).clear();
            Hands.saveHands();
        }
        return new CommandExecutionResult(new ReplyBuilder().newLine().attach("Shuffled...").newLine().toString(), 1);
    }
}
