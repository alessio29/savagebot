package org.alessio29.savagebot.commands.cards;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.characters.CharacterInitiative;
import org.alessio29.savagebot.commands.Category;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.exceptions.CardAlreadyDealtException;
import org.alessio29.savagebot.initiative.DrawCardResult;
import org.alessio29.savagebot.internal.CommandExecutionResult;
import org.alessio29.savagebot.internal.DealParams;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * 
 * @author aless
 */


public class DrawInitiativeCardCommand implements ICommand {

	@Override
	public String getName() {
		return "draw";
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.INITIATIVE;
	}

	@Override
	public String getDescription() {
		return "draws card to character\n"
				+ "add q if character has edge Quick\n"
				+ "add l if character has edge Levelheaded\n"
				+ "add il if character has edge Improved Levelheaded\n"
				+ "add h if character has hindrance Hesitant";
	}

	@Override
	public String[] getArguments() {
		String[] res = {"character", "[ilqh]"};
		return res;
	}
	
	private DealParams makeDealParams(String[] args) throws Exception {
		args = removeEmptyArgs(args);
		DealParams result = splitArgs(args);
		return result;
	}

	private DealParams splitArgs(String[] args) throws Exception {

		String charName = args[0].trim();
		if (charName.isEmpty()) {
			throw new Exception("First parameter must be non-empty character name!");
		}
		
		charName = charName.substring(0, 1).toUpperCase() + charName.substring(1);
		
		String edges = "";
		if (args.length>1) {
			String tmp = args[1].trim().toLowerCase();
			Pattern p = Pattern.compile("^[^ilqh]$");
		    Matcher m = p.matcher(tmp);  
			if (m.matches()) {
				throw new Exception("Third parameter must be a combination of letters: h - for Hesitant hindrance, q - for Quick edge, l - for Levelheaded edge and il for Improved Levelheaded edge!");
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

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());

		if(deck.isShuffleNeeded()) {
			return new CommandExecutionResult("Shuffle is needed..", 1);
		}
		if (args.length < 1) {
			return new CommandExecutionResult("Something is missing..", 2);
		}
		
		DealParams dealParams = makeDealParams(args);
		
		int argsCount = 1;
		if (!dealParams.getParams().isEmpty()) {
			argsCount = 2;
		}
		
		if (!CharacterInitCache.alreadyDealt(event.getGuild(), dealParams.getCharacterName())) {
			DrawCardResult cards = deck.getCardByParams(dealParams.getParams());
			if(cards == null) {
				throw new Exception("Deck is empty!");
			}
			try {
				CharacterInitCache.addCharacter(event.getGuild(), new CharacterInitiative(dealParams.getCharacterName(), cards));
			} catch (CardAlreadyDealtException e) {
				return new CommandExecutionResult("Card already dealt!", argsCount);
			}
		}
		
		StringBuilder reply = new StringBuilder();
		Set<CharacterInitiative> chars = CharacterInitCache.getCharacters(event.getGuild());
		if (chars!=null && !chars.isEmpty()) {
			for (CharacterInitiative c : CharacterInitCache.getCharacters(event.getGuild())) {
				String allCards = c.getAllCards().stream().map(cr ->cr.toString()).collect(Collectors.joining(","));
				reply.append(c.getName()).append(" - ").append(" - ").append(c.getBestCard().toString()).append("["+allCards+"]").append("\n");
			}
		} else {
			reply.append("No cards dealt!");
		}
		return new CommandExecutionResult(reply.toString(), argsCount+1);
	}


}