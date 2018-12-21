package com.github.alessio29.savagebot.commands.cards;

import java.util.List;
import java.util.stream.Collectors;

import com.github.alessio29.savagebot.cards.Deck;
import com.github.alessio29.savagebot.cards.Decks;
import com.github.alessio29.savagebot.cards.Hands;
import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class ShuffleDeckCommand implements ICommand {

	@Override
	public String getName() {
		return "shuffle";
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.CARDS;
	}

	@Override
	public String getDescription() {
		return "Shuffles current deck, resets secret cards dealt to all users in this channel";
	}

	@Override
	public String[] getArguments() {
		return null;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());
		deck.shuffle();	
		Guild guild = event.getGuild();

		List<User> users = event.getGuild().getMembers().stream().
				filter(m -> m.hasPermission(event.getTextChannel(), Permission.MESSAGE_READ)).
				filter(m->m.getOnlineStatus()==OnlineStatus.ONLINE).map(m -> m.getUser()).collect(Collectors.toList());
		for (User user : users) {
			Hands.getHand(guild, user).clear();
		}
		return new CommandExecutionResult("\nShuffled...\n", 1);
	}


}
