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
        if (storage.get(guild) == null) {
            return new HashSet<>();
        }
        if ((storage.get(guild).get(channel) == null)) {
            return new HashSet<>();
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
        Map<String, Set<Character>> m = storage.get(guild);
        if (m == null) {
            m = new HashMap();
        }
        m.put(channel, characters);
        storage.put(guild, m);
    }

    public static void storeAllCharacters(String guild, String channel, Set<Character> chars) {
        Map<String, Set<Character>> m = storage.get(guild);
        if (m == null) {
            m = new HashMap();
        }
        m.put(channel, chars);
        storage.put(guild, m);
    }

    public static void resetCharactersInitiative(String guildId, String channelId) {

        for (Character c : getCharacters(guildId, channelId)) {
            c.clearCards();
            c.setOutOfFight(true);
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
}