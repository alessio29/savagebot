package org.alessio29.savagebot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.alessio29.savagebot.internal.Commands;
import org.alessio29.savagebot.internal.ParseInputListener;
import org.alessio29.savagebot.internal.RedisClient;

import javax.security.auth.login.LoginException;

public class SavageBotRunner {

	private static String passwd;

	public static boolean passwdOk(String str) {
		if (passwd == null || passwd.isEmpty()) {
			return false;
		}
		return passwd.equals(str);
	}

	public static void main(String[] args) throws LoginException {

		if (args.length <4) {
			System.out.println("Parameters must be provided: token redisHost redisPort redisPass!");
			return;
		}
		passwd = args[0].trim();
		String token = args[1].trim();
		String redisHost = args[2].trim();
		int redisPort = Integer.parseInt(args[3].trim());
		String redisPass = args[4].trim();

		Commands.registerDefaultCommands();

		RedisClient.setup(redisHost, redisPort, redisPass);
		RedisClient.init(redisHost, redisPort, redisPass);

	    JDA jda = new JDABuilder(token).build();
        jda.addEventListener(new ParseInputListener());
	}

}
