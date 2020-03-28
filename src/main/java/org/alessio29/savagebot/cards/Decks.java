package org.alessio29.savagebot.cards;

import org.alessio29.savagebot.internal.RedisClient;
import org.alessio29.savagebot.internal.utils.JsonConverter;

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
	// guildId, channelId -> deck
	private static Map<String, Map<String, Deck>> decks = new HashMap<>();

	public static Deck getDeck(String guildId, String channelId) {

		boolean deckCreated = false;
		Map<String, Deck> guildDecks = decks.get(guildId);
		if(guildDecks == null) {
			Decks.addDeck(guildId, channelId, Deck.createNewDeck());
			deckCreated = true;
		}
		Deck result = decks.get(guildId).get(channelId);
		if (result == null || result.isEmpty()) {
			Decks.addDeck(guildId, channelId, Deck.createNewDeck());
			deckCreated = true;
		}
		if (deckCreated) {
			save2Redis();
		}
		return decks.get(guildId).get(channelId);
	}
		
	private static void addDeck(String guild, String channel, Deck deck) {
		Map<String, Deck> map = decks.computeIfAbsent(guild, k -> new HashMap<>());
		map.put(channel, deck);
		save2Redis();
	}

	public static void save2Redis() {

		Map<String, String> map = new HashMap<>();
		if (decks.keySet().isEmpty()) {
			return;
		}
		for (String guildID : decks.keySet()) {
			if (decks.get(guildID) == null || decks.get(guildID).keySet().isEmpty()) {
				continue;
			}
			for (String channelID : decks.get(guildID).keySet()) {
				if (decks.get(guildID).get(channelID) == null) {
					continue;
				}
				Deck d = decks.get(guildID).get(channelID);
				if (d == null) {
					continue;
				}
				String key = guildID + RedisClient.DELIMITER + channelID;
				map.put(key, RedisClient.asJson(d));
			}
		}
		RedisClient.saveMapAtKey(REDIS_DECKS_KEY, map);
	}

	public static void loadFromRedis() {
		Map<String, String> map = RedisClient.loadMapAtKey(REDIS_DECKS_KEY);
		for (String key : map.keySet()) {
			String[] keyParts = key.split(RedisClient.DELIMITER);
			if (keyParts.length < 3) {
				// key is wrong
				continue;
			}
			addDeck(keyParts[0], keyParts[1], JsonConverter.getInstance().fromJson(map.get(key), Deck.class));
		}
	}
}