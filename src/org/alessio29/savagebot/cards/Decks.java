package org.alessio29.savagebot.cards;

import java.util.HashMap;
import java.util.Map;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IChannel;

/**
 * 
 * @author aless
 * 
 * This class contains utility methods for working with decks  
 *
 */

public class Decks {
	
	private static Map<IGuild, Map<IChannel, Deck>> decks = new HashMap<IGuild, Map<IChannel, Deck>>();
	
	public static Deck getDeck(IGuild guild, IChannel channel) { 

		 Map<IChannel, Deck> guidlDecks = decks.get(guild);
		if(guidlDecks == null) {
			Decks.addDeck(guild, channel, Deck.createNewDeck());
		}
		return decks.get(guild).get(channel);
	}
		
	public static void addDeck(IGuild guild, IChannel channel,  Deck deck) {
		
		Map<IChannel, Deck> map = decks.get(guild);
		if (map == null) {
			map = new HashMap<>();
			decks.put(guild, map);
		}
		map.put(channel, deck);
	}
	
}