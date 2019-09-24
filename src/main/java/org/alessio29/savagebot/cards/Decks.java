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

	private static final String DECKS = "decks";

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

	public static void saveDecks() {

		Map <String, String> data = new HashMap<>();

		for (Map.Entry<Guild, Map<MessageChannel, Deck>> entry : decks.entrySet()) {
			String guildKey = entry.getKey().getId();
			Map<MessageChannel, Deck> map = entry.getValue();
			for (Map.Entry<MessageChannel, Deck> internalEntry : map.entrySet()) {
				String channelKey = internalEntry.getKey().getId();
			}
		}
		RedisClient.getClient().hset(DECKS, data);
	}
	
}