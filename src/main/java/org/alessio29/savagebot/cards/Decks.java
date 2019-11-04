package org.alessio29.savagebot.cards;

import org.alessio29.savagebot.internal.RedisClient;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author aless
 * 
 * This class contains utility methods for working with decks  
 *
 */

public class Decks {

	private static final String REDIS_DECKS_KEY = "decks";


	private static Map<String, Map<String, Deck>> decks = new HashMap<>();

	public static Deck getDeck(String guildId, String channelId) {

		 Map<String, Deck> guildDecks = decks.get(guildId);
		if(guildDecks == null) {
			Decks.addDeck(guildId, channelId, Deck.createNewDeck());
		}
		Deck result = decks.get(guildId).get(channelId);
		if (result == null ) {
			Decks.addDeck(guildId, channelId, Deck.createNewDeck());
		}
		return decks.get(guildId).get(channelId);
	}
		
	private static void addDeck(String guild, String channel, Deck deck) {
		Map<String, Deck> map = decks.computeIfAbsent(guild, k -> new HashMap<>());
		map.put(channel, deck);
		saveDecks();
	}

	public static void saveDecks() {
		RedisClient.storeObject(REDIS_DECKS_KEY, decks);
	}

	public static void loadDecks() {
		decks = RedisClient.loadObject(REDIS_DECKS_KEY, HashMap.class);
		if (decks == null) {
			decks = new HashMap<>();
		}
	}

}