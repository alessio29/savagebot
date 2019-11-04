package org.alessio29.savagebot.characters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Characters {

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

//    public static void saveCharacters() {
//        RedisClient.storeObject(CHARACTERS_REDIS_KEY, storage);
//    }
//
//    public static void loadCharacters() {
//        storage = RedisClient.loadObject(CHARACTERS_REDIS_KEY, HashMap.class);
//        if (storage == null) {
//            storage = new HashMap<>();
//        }
//    }
}
