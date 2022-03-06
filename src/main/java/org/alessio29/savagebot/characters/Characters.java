package org.alessio29.savagebot.characters;

import org.alessio29.savagebot.internal.RedisClient;
import org.alessio29.savagebot.internal.utils.JsonConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Characters {

    private static final Logger log = LogManager.getLogger(Characters.class.getName());
    private static final String REDIS_CHARACTERS_KEY = "characters";
    //                 guildId,    channelId    charName
    private static final Map<String, Map<String, Map<String, Character>>> characters = new HashMap<>();

    public static Map<String, Character> getCharacters(String guild, String channel) {
        characters.computeIfAbsent(guild, k -> new HashMap<>());
        return characters.get(guild).computeIfAbsent(channel, k -> new HashMap<>());
    }

    public static Character getCharacterByName(String guild, String channel, String name) {
        Map<String, Character> map = getCharacters(guild, channel);
        return map.get(name);
    }

    public static Character getByNameOrCreate(String guild, String channel, String name) {
        Map<String, Character> map = getCharacters(guild, channel);
        Character ch = map.get(name);
        if (ch == null) {
            ch = new Character(name);
            map.put(name, ch);
        }
        return ch;
    }

    public static void storeCharacter(String guild, String channel, Character character) {
        Map<String, Character> map = getCharacters(guild, channel);
        map.put(character.getName(), character);
        save2Redis();
    }

    public static void resetCharactersInitiative(String guildId, String channelId) {
        Map<String, Character> map = getCharacters(guildId, channelId);
        if (map.entrySet().isEmpty()) {
            return;
        }
        for (Map.Entry<String, Character> e :  map.entrySet()) {
            e.getValue().removeFromFight();
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

    public static void removeCharacter(String guildId, String channelId, String charName) {
        Map<String, Character> charMap = getCharacters(guildId, channelId);
        charMap.remove(charName);
        removeFromRedis(guildId, channelId, charName);
    }

    public static void removeAllCharacters(String guildId, String channelId) {
        Map<String, Character> charMap = getCharacters(guildId, channelId);
        for (String name : charMap.keySet()) {
            removeFromRedis(guildId, channelId, name);
        }
        charMap.clear();
    }


    private static void removeFromRedis(String guildId, String channelId, String charName) {
       RedisClient.remove(Characters.REDIS_CHARACTERS_KEY, getKey(guildId, channelId, charName));
    }

    private static void save2Redis() {

        Map<String, String> map = new HashMap<>();
        if (characters.keySet().isEmpty()) {
            return;
        }
        for (String guildID : characters.keySet()) {
            if (characters.get(guildID)==null || characters.get(guildID).keySet().isEmpty()) {
                continue;
            }
            for (String channelID : characters.get(guildID).keySet()) {
                if (characters.get(guildID).get(channelID)==null || characters.get(guildID).get(channelID).keySet().isEmpty()) {
                    continue;
                }
                for (String charName : characters.get(guildID).get(channelID).keySet()){
                    Character ch = characters.get(guildID).get(channelID).get(charName);
                    if (ch == null) {
                        continue;
                    }
                    String key = getKey(guildID, channelID, charName);
                    map.put(key, RedisClient.asJson(ch));
                }
            }
        }
        RedisClient.saveMapAtKey(REDIS_CHARACTERS_KEY, map);
    }

    @NotNull
    private static String getKey(String guildID, String channelID, String charName) {
        return guildID+ RedisClient.DELIMITER+channelID+RedisClient.DELIMITER+charName;
    }

    public static void loadFromRedis() {
        Map<String, String> map = RedisClient.loadMapAtKey(REDIS_CHARACTERS_KEY);
        for (String key : map.keySet()) {
            String[] keyParts = key.split(RedisClient.DELIMITER);
            if (keyParts.length <3 ) {
                // key is wrong
                continue;
            }
            String guildId = keyParts[0];
            String channelId =keyParts[1];

            try {
                JsonConverter converter = JsonConverter.getInstance();
                String json2convert = map.get(key);
                if (json2convert == null) {
                    log.debug("Nothing to convert - null value with key = " +key );
                    continue;
                }
                Character character = converter.fromJson(json2convert, Character.class);
                if (character == null) {
                    log.debug("Cannot deserialize character from value with key = " +key );
                    continue;
                }
                Map<String, Character> chars = getCharacters(guildId, channelId);
                chars.put(character.getName(), character);
            } catch (Exception e) {
                log.debug("Error while reading character from Redis storage.", e);
            }
        }
    }
}