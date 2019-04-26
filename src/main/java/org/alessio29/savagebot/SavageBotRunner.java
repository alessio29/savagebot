package org.alessio29.savagebot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.alessio29.savagebot.commands.admin.PingCommand;
import org.alessio29.savagebot.commands.admin.PrefixCommand;
import org.alessio29.savagebot.commands.bennies.GetBennyCommand;
import org.alessio29.savagebot.commands.bennies.HatCommand;
import org.alessio29.savagebot.commands.bennies.ShowPocketCommand;
import org.alessio29.savagebot.commands.bennies.UseBennyCommand;
import org.alessio29.savagebot.commands.cards.*;
import org.alessio29.savagebot.commands.dice.RollDiceCommand;
import org.alessio29.savagebot.commands.info.HelpCommand;
import org.alessio29.savagebot.commands.info.InviteCommand;
import org.alessio29.savagebot.commands.initiative.NewFightCommand;
import org.alessio29.savagebot.commands.initiative.NewRoundCommand;
import org.alessio29.savagebot.commands.initiative.ShowInitiativeCommand;
import org.alessio29.savagebot.internal.CommandRegistry;
import org.alessio29.savagebot.internal.ParseInputListener;

import javax.security.auth.login.LoginException;

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
