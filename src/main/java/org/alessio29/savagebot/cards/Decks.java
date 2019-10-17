package org.alessio29.savagebot.cards;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
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


	private static Map<Guild, Map<MessageChannel, Deck>> decks = new HashMap<>();

	public static Deck getDeck(Guild guild, MessageChannel channel) { 

		 Map<MessageChannel, Deck> guildDecks = decks.get(guild);
		if(guildDecks == null) {
			Decks.addDeck(guild, channel, Deck.createNewDeck());
		}
		Deck result = decks.get(guild).get(channel);
		if (result == null ) {
			Decks.addDeck(guild, channel, Deck.createNewDeck());
		}
		return decks.get(guild).get(channel);
	}
		
	private static void addDeck(Guild guild, MessageChannel channel, Deck deck) {
		Map<MessageChannel, Deck> map = decks.computeIfAbsent(guild, k -> new HashMap<>());
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