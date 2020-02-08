package org.alessio29.savagebot.characters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Characters {

    private static final String REDIS_CHARACTERS_KEY = "characters";
    //                 guildId,    channelId    charName
    private static Map<String, Map<String, Map<String, Character>>> characters = new HashMap<>();

    public static Map<String, Character> getCharacters(String guild, String channel) {

        characters.computeIfAbsent(guild, k -> new HashMap<>());
        characters.get(guild).computeIfAbsent(channel, k -> new HashMap<>());
        return characters.get(guild).get(channel);
    }

    public static Character getCharacterByName(String guild, String channel, String name) {
        Map<String, Character> map = getCharacters(guild, channel);
        return map.get(name);
    }

    public static void storeCharacter(String guild, String channel, Character character) {
        Map<String, Character> map = getCharacters(guild, channel);
        map.put(character.getName(), character);
    }

    public static void resetCharactersInitiative(String guildId, String channelId) {
        Map<String, Character> map = getCharacters(guildId, channelId);
        for (Map.Entry<String, Character> e :  map.entrySet()) {
            e.getValue().clearCards();
            if (e.getValue().isEmpty()) {
                map.remove(e);
            }
        }
    }

    public static Set<Character> getFightingCharacters(String guildId, String channelId) {
        return getCharacters(guildId, channelId).values().stream().
                filter(character -> !character.isOutOfFight()).
                collect(Collectors.toSet());
    }

    public static Set<Character> getFightingCharactersWithCards(String guildId, String channelId) {
        return getCharacters(guildId, channelId).values().stream().
                filter(character -> !character.isOutOfFight()).
                filter(character -> character.getBestCard() != null).
                collect(Collectors.toSet());
    }

    public static void clearCharactersCards(String guildId, String channelId) {
        for (Character c : getCharacters(guildId, channelId).values()) {
            c.clearCards();
        }
    }

    public static Set<Character> getCharactersWithTokens(String guildId, String channelId) {
        return getCharacters(guildId, channelId).values().stream().
                filter(character -> character.getTokens() != null).
                collect(Collectors.toSet());
    }

    public static void removeCharacter(String guildId, String channelId, String charName) {
        Map<String, Character> charMap = getCharacters(guildId, channelId);
        charMap.remove(charName);
    }
}