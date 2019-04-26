package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class Users {
	
	public static User findUser(String id, Guild guild) throws Exception {
		
		String userID = Users.checkUserId(id);
		User user = null; 
		try {
			user = guild.getJDA().getUserById(userID);
			if (user == null ) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		return user;
	}
	
	private static String checkUserId (String id) throws Exception {

		String userID = id.replaceAll("[<>@!]", "");
		for(char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
			if(id.toUpperCase().indexOf(alphabet) >= 0){
				throw new Exception(new IllegalArgumentException(id + " is not a valid user!"));
			}
		}
		return userID;
	}
}
