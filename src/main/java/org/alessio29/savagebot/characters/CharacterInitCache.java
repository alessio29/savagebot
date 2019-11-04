package org.alessio29.savagebot.characters;

import org.alessio29.savagebot.exceptions.CardAlreadyDealtException;

import java.util.*;



public class CharacterInitCache {

	private static Map<String, HashSet<CharacterInitiative>> characters = new HashMap<>();

	public static Set<CharacterInitiative> getCharacters(String guild) {
		
		Set<CharacterInitiative> chars =  characters.get(guild);
		if (chars == null) {
			return Collections.emptySet();
		}
		return new TreeSet<CharacterInitiative>(chars);
	}

	public static void resetCharactersInitiative(String guild) {
		if (characters.get(guild)!=null) {
			characters.get(guild).clear();
		}
	}
	
	public static void addCharacter(String guild, CharacterInitiative c) throws CardAlreadyDealtException {
		
		if (characters.get(guild)==null) {
			characters.put(guild, new HashSet<CharacterInitiative>());
		}
		if (characters.get(guild).contains(c)) {
			throw new CardAlreadyDealtException("Character "+c.getName()+" has already dealt card!");
		}
		characters.get(guild).add(c);
	}

	public static boolean alreadyDealt(String guild, String characterName) {
		if (characters.get(guild)==null) {
			characters.put(guild, new HashSet<CharacterInitiative>());
		}
		return characters.get(guild).contains(new CharacterInitiative(characterName));
	}
}