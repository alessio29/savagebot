package org.alessio29.savagebot.commands.initiative;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.commands.CommandCategory;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;



public class NewFightCommand implements ICommand {

	@Override
	public String getName() {
		
		return "fight";
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
		
		return "starts new fight: shuffles deck, resets intiative tracker";
	}

	@Override
	public String[] getArguments() {
		
		return null;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		Decks.getDeck(event.getGuild(), event.getChannel()).shuffle();
		CharacterInitCache.resetCharactersInitiative(event.getGuild());
		Rounds.resetRounds(event.getGuild(), event.getTextChannel());
		return new CommandExecutionResult("Deck is shuffled, initiative tracker reset, starting new fight.\n  ========== Round 1 ========== ", 1);
	}

}
