package com.github.alessio29.savagebot.internal;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.entities.Guild;

public class Prefixes {

	public static final String DEFAULT_PREFIX = ".";
	private static Map<Guild, String> prefixes = new HashMap<>();
	
	public static String getPrefix(Guild server) {
		String res = prefixes.get(server);
		return res==null?DEFAULT_PREFIX:res;
	}

	public static String setPrefix(Guild server, String prefix) {
		return prefixes.put(server, prefix);
	}
}
