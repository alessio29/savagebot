package com.github.alessio29.savagebot.cards;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;


/**
 * 
 * @author aless
 * 
 * This class contains utility methods for working with decks  
 *
 */

public class Decks {
	
	private static Map<Guild, Map<MessageChannel, Deck>> decks = new HashMap<Guild, Map<MessageChannel, Deck>>();
	
	public static Deck getDeck(Guild guild, MessageChannel channel) { 

		 Map<MessageChannel, Deck> guidlDecks = decks.get(guild);
		if(guidlDecks == null) {
			Decks.addDeck(guild, channel, Deck.createNewDeck());
		}
		Deck result = decks.get(guild).get(channel);
		if (result == null ) {
			Decks.addDeck(guild, channel, Deck.createNewDeck());
		}
		return decks.get(guild).get(channel);
	}
		
	public static void addDeck(Guild guild, MessageChannel channel,  Deck deck) {
		
		Map<MessageChannel, Deck> map = decks.get(guild);
		if (map == null) {
			map = new HashMap<>();
			decks.put(guild, map);
		}
		map.put(channel, deck);
	}
	
}