package org.alessio29.savagebot.characters;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.alessio29.savagebot.exceptions.CardAlreadyDealtException;

import sx.blah.discord.handle.obj.IGuild;

public class CharacterInitCache {

	private static Map<IGuild, HashSet<CharacterInitiative>> characters = new HashMap<>();

	public static Set<CharacterInitiative> getCharacters(IGuild guild) {
		
		Set<CharacterInitiative> chars =  characters.get(guild);
		if (chars == null) {
			return Collections.emptySet();
		}
		return new TreeSet<CharacterInitiative>(chars);
	}

	public static void resetCharactersInitiative(IGuild guild) {
		if (characters.get(guild)!=null) {
			characters.get(guild).clear();
		}
	}
	
	public static Set<CharacterInitiative> addCharacter(IGuild guild, CharacterInitiative c) throws CardAlreadyDealtException {
		
		if (characters.get(guild)==null) {
			characters.put(guild, new HashSet<CharacterInitiative>());
		}
		if (characters.get(guild).contains(c)) {
			throw new CardAlreadyDealtException("Character "+c.getName()+" has already dealt card!");
		}
		characters.get(guild).add(c);
		return new TreeSet<CharacterInitiative>(characters.get(guild));
	}

	public static boolean alreadyDealt(IGuild guild, String characterName) {
		if (characters.get(guild)==null) {
			characters.put(guild, new HashSet<CharacterInitiative>());
		}
		return characters.get(guild).contains(new CharacterInitiative(characterName, null));
	}
	
}
