package com.github.alessio29.savagebot.characters;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.github.alessio29.savagebot.exceptions.CardAlreadyDealtException;

import net.dv8tion.jda.core.entities.Guild;



public class CharacterInitCache {

	private static Map<Guild, HashSet<CharacterInitiative>> characters = new HashMap<>();

	public static Set<CharacterInitiative> getCharacters(Guild guild) {
		
		Set<CharacterInitiative> chars =  characters.get(guild);
		if (chars == null) {
			return Collections.emptySet();
		}
		return new TreeSet<CharacterInitiative>(chars);
	}

	public static void resetCharactersInitiative(Guild guild) {
		if (characters.get(guild)!=null) {
			characters.get(guild).clear();
		}
	}
	
	public static void addCharacter(Guild guild, CharacterInitiative c) throws CardAlreadyDealtException {
		
		if (characters.get(guild)==null) {
			characters.put(guild, new HashSet<CharacterInitiative>());
		}
		if (characters.get(guild).contains(c)) {
			throw new CardAlreadyDealtException("Character "+c.getName()+" has already dealt card!");
		}
		characters.get(guild).add(c);
	}

	public static boolean alreadyDealt(Guild guild, String characterName) {
		if (characters.get(guild)==null) {
			characters.put(guild, new HashSet<CharacterInitiative>());
		}
		return characters.get(guild).contains(new CharacterInitiative(characterName));
	}
}