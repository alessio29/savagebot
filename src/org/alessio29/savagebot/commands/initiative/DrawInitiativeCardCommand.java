package org.alessio29.savagebot.commands.initiative;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitiative;
import org.alessio29.savagebot.exceptions.CardAlreadyDealtException;
import org.alessio29.savagebot.internal.DealParams;
import org.alessio29.savagebot.internal.Messages;
import org.alessio29.savagebot.characters.CharacterInitCache;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;
import com.Cardinal.CommandPackage.Exceptions.MissingRequirementsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

/**
 * 
 * @author aless
 * 
 * This command deals cards to PC or NPC
 * 
 * Syntax: deal [n]pc name [ilq]
 * 
 */


public class DrawInitiativeCardCommand implements ICommand {

	@Override
	public String getName() {
		return "draw";
	}

	@Override
	public Category getCategory() {
		return null;
	}

	@Override
	public String getDescription() {
		return "Deal one initiative card";
	}

	@Override
	public String[] getArguments() {
		return null;
	}
	
	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix)
			throws MissingRequirementsException, MissingArgumentsException {

		Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());

		if(deck.isShuffleNeeded()) {
			event.getChannel().sendMessage("Перемешать бы надо..");
			return;
		}
		if (args.length < 2) {
			event.getChannel().sendMessage("Не хватает чего-то..");
			return;
		}
		
		DealParams dealParams = makeDealParams(args);
		if (!CharacterInitCache.alreadyDealt(event.getGuild(), dealParams.getCharacterName())) {
			Card card = deck.getCardByParams(dealParams.getParams());
			try {
				CharacterInitCache.addCharacter(event.getGuild(), new CharacterInitiative(dealParams.getCharacterName(), card));
			} catch (CardAlreadyDealtException e) {
				IChannel ch = event.getChannel();
				ch.sendMessage(e.getMessage());
			}
		}
		Messages.showOrder(event);
	}
	
	
	private DealParams makeDealParams(String[] args) throws MissingArgumentsException {
		
		args = removeEmptyArgs(args);
		DealParams result = splitArgs(args);
		
		return result;
	}

	private DealParams splitArgs(String[] args) throws MissingArgumentsException {

		String charName = args[1].trim();
		if (charName.isEmpty()) {
			throw new MissingArgumentsException("First parameter must be non-empty character name!");
		}
		
		charName = charName.substring(0, 1).toUpperCase() + charName.substring(1);
		
		String edges = "";
		if (args.length>3) {
			String tmp = args[3].trim().toLowerCase();
			Pattern p = Pattern.compile("^[^ilq]$");
		    Matcher m = p.matcher(tmp);  
			if (m.matches()) {
				throw new MissingArgumentsException("Third parameter must combination of letters: q - for Quick edge, l - for Levelheaded edge and il for Improved Levelheaded edge!");
			}
			edges = tmp;
		}
		return new DealParams(charName, edges);
	}

	private String[] removeEmptyArgs(String[] args) {

		String[] tmp = new String[args.length];
		
		int i=0;
		for (String s : args) {
			if (s.trim().isEmpty()) {
				continue;
			}
			tmp[i]=s;
			i++;
		}
		String[] result = new String[i];
		System.arraycopy(tmp, 0, result, 0, i);
		return result; 
	}


}