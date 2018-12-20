package com.github.alessio29.savagebot;

import javax.security.auth.login.LoginException;

import com.github.alessio29.savagebot.commands.admin.PingCommand;
import com.github.alessio29.savagebot.commands.admin.PrefixCommand;
import com.github.alessio29.savagebot.commands.bennies.GetBennyCommand;
import com.github.alessio29.savagebot.commands.bennies.HatCommand;
import com.github.alessio29.savagebot.commands.bennies.ShowPocketCommand;
import com.github.alessio29.savagebot.commands.bennies.UseBennyCommand;
import com.github.alessio29.savagebot.commands.cards.DealCardCommand;
import com.github.alessio29.savagebot.commands.cards.DrawInitiativeCardCommand;
import com.github.alessio29.savagebot.commands.cards.OpenCardCommand;
import com.github.alessio29.savagebot.commands.cards.ShowCardsCommand;
import com.github.alessio29.savagebot.commands.cards.ShuffleDeckCommand;
import com.github.alessio29.savagebot.commands.dice.RollDiceCommand;
import com.github.alessio29.savagebot.commands.info.HelpCommand;
import com.github.alessio29.savagebot.commands.info.InviteCommand;
import com.github.alessio29.savagebot.commands.initiative.NewFightCommand;
import com.github.alessio29.savagebot.commands.initiative.NewRoundCommand;
import com.github.alessio29.savagebot.commands.initiative.ShowInitiativeCommand;
import com.github.alessio29.savagebot.internal.CommandRegistry;
import com.github.alessio29.savagebot.internal.ParseInputListener;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class SavageBotRunner {

	public static void main(String[] args) throws LoginException {

		if (args.length <1) {
			System.out.println("Token must be provided as parameter!");
			return;
		}
		String token = args[0].trim();
		
		registerCommands();		
	    JDA jda = new JDABuilder(token).build();
        jda.addEventListener(new ParseInputListener());

	}
	
	private static void registerCommands() { 
		
		// admin commands
		CommandRegistry.registerCommand(new PingCommand());
		CommandRegistry.registerCommand(new PrefixCommand());
		
		// info commands
		CommandRegistry.registerCommand(new InviteCommand());
		CommandRegistry.registerCommand(new HelpCommand());
		
		// bennies commands
		CommandRegistry.registerCommand(new GetBennyCommand());
		CommandRegistry.registerCommand(new HatCommand());
		CommandRegistry.registerCommand(new ShowPocketCommand());
		CommandRegistry.registerCommand(new UseBennyCommand());
		
		// cards commands
		CommandRegistry.registerCommand(new DealCardCommand());
		CommandRegistry.registerCommand(new DrawInitiativeCardCommand());
		CommandRegistry.registerCommand(new OpenCardCommand());
		CommandRegistry.registerCommand(new ShowCardsCommand());
		CommandRegistry.registerCommand(new ShuffleDeckCommand());
		
		// dice commands
        CommandRegistry.registerCommand(new RollDiceCommand());
        
        // initiative commands
        CommandRegistry.registerCommand(new NewFightCommand());
        CommandRegistry.registerCommand(new NewRoundCommand());
        CommandRegistry.registerCommand(new ShowInitiativeCommand());
	}
}	
