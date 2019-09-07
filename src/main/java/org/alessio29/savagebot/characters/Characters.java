package org.alessio29.savagebot.characters;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;

import java.util.*;


public class Characters {

    private static Map<Guild, Map<Channel, Set<Character>>> storage = new HashMap<>();


    public static Set<Character> getCharacters(Guild guild, Channel channel) {
        if (storage.get(guild) == null) {
            return new HashSet<>();
        }
        if ((storage.get(guild).get(channel) == null)) {
            return new HashSet<>();
        }
        return storage.get(guild).get(channel);
    }

    public static Character getCharacterByName(Guild guild, Channel channel, String name) {
        Set<Character> list = getCharacters(guild, channel);
        for (Character c : list) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public static void storeCharacter(Guild guild, Channel channel, Character character) {
        Set<Character> characters = getCharacters(guild, channel);
        characters.add(character);
        Map<Channel, Set<Character>> m = storage.get(guild);
        if (m == null) {
            m = new HashMap();
        }
        m.put(channel, characters);
        storage.put(guild, m);
    }
}
