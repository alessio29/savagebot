package org.alessio29.savagebot.variables;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;


public class Variables {

	
	private static Map<Guild, Map<User, Map<String, String>>> variables = new HashMap<> ();
	
	public static String getVariable(Guild guild, User user, String name) {
		
		Map<User, Map<String, String>> map = variables.get(guild); 
		if (map!=null && map.get(user)!=null) {
			return map.get(user).get(name);
		}
		return null;
	}
	
	public static Map<String, String> getVariables(Guild guild, User user) {
		
		Map<User, Map<String, String>> map = variables.get(guild); 
		if (map!=null && map.get(user)!=null) {
			return map.get(user);
		}
		return new HashMap<>();
	}
	
	
	public static void addVariable(Guild guild, User user, String name, String value) {
		
		Map<User, Map<String, String>> map = variables.get(guild);
		if (map==null) {
			map = new HashMap<>();
		}
		variables.put(guild, map);
		Map<String, String> vars = map.get(user);
		if (vars==null) {
			vars = new HashMap<>();
		}
		map.put(user, vars);
		vars.put(name, value);
	}
}
