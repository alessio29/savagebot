package org.alessio29.savagebot.commands.initiative;


import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.internal.commands.CommandCategory;
import org.alessio29.savagebot.internal.commands.ICommand;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;


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
	public CommandCategory getCategory() {
		return CommandCategory.INITIATIVE;
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
		String message = ""; 
		if (deck.isJokerDealt()) {
			deck.shuffle();
			deck.setJokerDealt(false);
			message = " Joker was dealt in last round, deck is shuffled.\n" ;
		}
		message +=" ========== Round "+round+" ========== ";
		return new CommandExecutionResult(message, 1);
	}

}
