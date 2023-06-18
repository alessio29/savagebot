package org.alessio29.savagebot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.*;
import org.alessio29.savagebot.internal.commands.Commands;

import javax.security.auth.login.LoginException;

public class SavageBotRunner {

	private static String passwd;

	public static void main(String[] args) throws LoginException {

		if (args.length < 2) {
			System.out.println("Parameters must be provided: password token redisHost redisPort redisPass");
			return;
		}
		passwd = args[0].trim();
		String token = args[1].trim();


		if (args.length >= 5) {
			String host = args[2].trim();
			int port = Integer.parseInt(args[3].trim());
			String pass = (args[4].equals("dummyPass")) ? null : args[4];
			RedisClient.setup(host, port, pass);
			Prefixes.loadFromRedis();
			Decks.loadFromRedis();
			Hands.loadFromRedis();
			Characters.loadFromRedis();
		} else {
			System.out.println("Starting without redis, some functionality is unavailable.");
		}

		if (args.length >= 6) {
			String debug = args[5].trim();
			if (debug.equalsIgnoreCase("debug")) {
				Prefixes.setDebugPrefix();
			}
		}

		ShardManager shardManager = DefaultShardManagerBuilder.createDefault(token)
				.addEventListeners(new ParseInputListener(), new DiscordSlashCommandListener())
				.build();

		for (JDA jda : shardManager.getShards()) {
			Commands.registerDefaultCommands(jda);
			SelfMentionContainer.initialize(jda.getSelfUser().getAsMention());
		}
	}

	public static boolean passwdOk(String str) {
		if (passwd == null || passwd.isEmpty()) {
			return false;
		}
		return passwd.equals(str);
	}


}
