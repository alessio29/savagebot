package org.alessio29.savagebot.characters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Characters {

    //                 guildId,    channelId
    private static Map<String, Map<String, Set<Character>>> storage = new HashMap<>();
    private static final String CHARACTERS_REDIS_KEY = "characters";

    public static Set<Character> getCharacters(String guild, String channel) {

        Set<Character> result = null;
        if (storage.get(guild) == null) {
            storage.put(guild, new HashMap<>());
        }
        if (storage.get(guild).get(channel) == null)  {
            result = new HashSet<>();
            storage.get(guild).put(channel, result);
        }
        return storage.get(guild).get(channel);
    }

    public static Character getCharacterByName(String guild, String channel, String name) {
        Set<Character> list = getCharacters(guild, channel);
        for (Character c : list) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public static void storeCharacter(String guild, String channel, Character character) {
        Set<Character> characters = getCharacters(guild, channel);
        characters.add(character);
    }

    public static void storeAllCharacters(String guild, String channel, Set<Character> chars) {
        Set<Character> characters = getCharacters(guild, channel);
        characters.addAll(chars);
    }

    public static void resetCharactersInitiative(String guildId, String channelId) {

        for (Character c : getCharacters(guildId, channelId)) {
            c.removeFroFight();
        }
    }

    public static Set<Character> getFightingCharacters(String guildId, String channelId) {
        return getCharacters(guildId, channelId).stream().
                filter(character -> !character.isOutOfFight()).
                collect(Collectors.toSet());
    }

    public static Set<Character> getFightingCharactersWithCards(String guildId, String channelId) {
        return getCharacters(guildId, channelId).stream().
                filter(character -> !character.isOutOfFight()).
                filter(character -> character.getBestCard()!=null).
                collect(Collectors.toSet());
    }

    public static void clearCharactersCards(String guildId, String channelId) {
        for (Character c : getCharacters(guildId, channelId)) {
            c.clearCards();
        }
    }

    public static Set<Character> getCharactersWithTokens(String guildId, String channelId) {
        return getCharacters(guildId, channelId).stream().
                filter(character -> character.getTokens()!=null).
                collect(Collectors.toSet());
    }
}