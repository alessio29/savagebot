package org.alessio29.savagebot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.Prefixes;
import org.alessio29.savagebot.internal.RedisClient;
import org.alessio29.savagebot.internal.commands.Commands;
import org.alessio29.savagebot.internal.ParseInputListener;

import javax.security.auth.login.LoginException;

public class SavageBotRunner {

	private static String passwd;

	public static void main(String[] args) throws LoginException {

		if (args.length <5) {
			System.out.println("Parameters must be provided: password token redisHost redisPort redisPass");
			return;
		}
		passwd = args[0].trim();
		String token = args[1].trim();

		Commands.registerDefaultCommands();

		String host = args[2].trim();
		int port = Integer.parseInt(args[3].trim());
		String pass = args[4];

		RedisClient.setup(host, port, pass);

		Prefixes.loadFromRedis();
		Decks.loadFromRedis();
		Hands.loadFromRedis();
		Characters.loadFromRedis();

		if (args.length >=6) {
			String debug = args[5].trim();
			if (debug.equalsIgnoreCase("debug")) {
				Prefixes.setDebugPrefix();
			}
		}

	    JDA jda = new JDABuilder(token).build();
        jda.addEventListener(new ParseInputListener());
	}

	public static boolean passwdOk(String str) {
		if (passwd == null || passwd.isEmpty()) {
			return false;
		}
		return passwd.equals(str);
	}
}
