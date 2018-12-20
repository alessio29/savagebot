package com.github.alessio29.savagebot.commands.initiative;


import com.github.alessio29.savagebot.cards.Deck;
import com.github.alessio29.savagebot.cards.Decks;
import com.github.alessio29.savagebot.characters.CharacterInitCache;
import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.initiative.Rounds;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class NewRoundCommand implements ICommand {

	@Override
	public String getName() {
		return "round";
	}

	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.INITIATIVE;
	}

	@Override
	public String getDescription() {
		return "Starts new round: resets resets intiative tracker, shuffles deck, if joker was dealt on previous round";
	}

	@Override
	public String[] getArguments() {
		return null;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		Guild guild = event.getGuild();
		Rounds.nextRound(guild, event.getTextChannel());
		Integer round = Rounds.getGuildRound(guild, event.getTextChannel());
		Deck deck = Decks.getDeck(guild, event.getChannel());
		CharacterInitCache.resetCharactersInitiative(guild);
		if (deck.isJokerDealt()) {
			deck.shuffle();
			deck.setJokerDealt(false);
			return new CommandExecutionResult(" Joker was dealt in last round, deck is shuffled. ", 1);
		}
		return new CommandExecutionResult(" ========== Round "+round+" ========== ", 1);
	}

}
