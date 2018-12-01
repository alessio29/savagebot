package org.alessio29.savagebot.commands.initiative;

import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.initiative.Rounds;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;
import com.Cardinal.CommandPackage.Exceptions.MissingRequirementsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class NewFightCommand implements ICommand {

	@Override
	public String getName() {
		
		return "fight";
	}

	@Override
	public Category getCategory() {
		
		return null;
	}

	@Override
	public String getDescription() {
		
		return null;
	}

	@Override
	public String[] getArguments() {
		
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix)
			throws MissingRequirementsException, MissingArgumentsException {

		Decks.getDeck(event.getGuild(), event.getChannel()).shuffle();
		CharacterInitCache.resetCharactersInitiative(event.getGuild());
		Rounds.resetRounds(event.getGuild(), event.getChannel());
		event.getChannel().sendMessage("Колода помешана, порядок хода обнулен, начинаем бой.");
		event.getChannel().sendMessage(" ========== Раунд 1 ========== ");
		return;
	}

}
