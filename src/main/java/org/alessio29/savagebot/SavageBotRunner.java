package org.alessio29.savagebot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.alessio29.savagebot.internal.Commands;
import org.alessio29.savagebot.internal.ParseInputListener;
import org.alessio29.savagebot.internal.RedisClient;

import javax.security.auth.login.LoginException;

public class SavageBotRunner {

	public static void main(String[] args) throws LoginException {

		if (args.length <4) {
			System.out.println("Parameters must be provided: token redisHost redisPort redisPass!");
			return;
		}
		String token = args[0].trim();
		String redisHost = args[1].trim();
		int redisPort = Integer.parseInt(args[2].trim());
		String redisPass = args[3].trim();

		Commands.registerDefaultCommands();
		RedisClient.init(redisHost, redisPort, redisPass);

	    JDA jda = new JDABuilder(token).build();
        jda.addEventListener(new ParseInputListener());
	}

}
