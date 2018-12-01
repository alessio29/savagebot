package org.alessio29.savagebot.internal;

import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Users {

	
	public static IUser findUser(String id, IGuild guild) throws MissingArgumentsException {
		
		String userID = Users.checkUserId(id);
		IUser user = null; 
				
		try {
			user = guild.getUserByID(Long.parseLong(userID));
			if (user == null ) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		return user;
	}
	
	
	private static String checkUserId (String id) throws MissingArgumentsException {

		String userID = id.replaceAll("[<>@!]", "");
		for(char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
			if(id.toUpperCase().indexOf(alphabet) >= 0){
				throw new MissingArgumentsException(new IllegalArgumentException(id + " is not a valid user!"));
			}
		}
		return userID;
	}
}
