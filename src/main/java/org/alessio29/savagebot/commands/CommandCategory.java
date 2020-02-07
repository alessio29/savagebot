package org.alessio29.savagebot.commands;

public enum CommandCategory implements Comparable<CommandCategory>{
	
	CARDS,
	DICE,
	BENNIES,
	INITIATIVE,
	INFO, 
	ADMIN,
	TOKENS,
	STATES,
	OTHER;

    public static CommandCategory valueOfOrNull(String string) {
		try {
			return valueOf(string);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
