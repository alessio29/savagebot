package org.alessio29.savagebot.commands.initiative;

import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.initiative.Rounds;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;
import com.Cardinal.CommandPackage.Exceptions.MissingRequirementsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;

public class NewRoundCommand implements ICommand {

	
	
	@Override
	public String getName() {
		return "round";
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return Category.UTILITY;
	}

	@Override
	public String getDescription() {
		return "Starts new round: resets resets intiative tracker, shuffles deck, if joker was dealt on previous round\n";
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix)
			throws MissingRequirementsException, MissingArgumentsException {

		IGuild guild = event.getGuild();
		Rounds.nextRound(guild, event.getChannel());
		Integer round = Rounds.getGuildRound(guild, event.getChannel());
		Deck deck = Decks.getDeck(guild, event.getChannel());
		CharacterInitCache.resetCharactersInitiative(guild);
		if (deck.isJokerDealt()) {
			deck.shuffle();
			deck.setJokerDealt(false);
			event.getChannel().sendMessage(" Joker was dealt in last round, deck is shuffled. ");
		}
		event.getChannel().sendMessage(" ========== Round "+round+" ========== ");
		return;
	}

}
