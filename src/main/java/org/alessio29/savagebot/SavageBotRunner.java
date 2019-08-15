package org.alessio29.savagebot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.alessio29.savagebot.internal.Commands;
import org.alessio29.savagebot.internal.ParseInputListener;

import javax.security.auth.login.LoginException;

public class SavageBotRunner {

	public static void main(String[] args) throws LoginException {

		if (args.length <1) {
			System.out.println("Token must be provided as parameter!");
			return;
		}
		String token = args[0].trim();

		Commands.registerDefaultCommands();

	    JDA jda = new JDABuilder(token).build();
        jda.addEventListener(new ParseInputListener());
	}

}	
