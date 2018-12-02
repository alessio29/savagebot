package org.alessio29.savagebot;

import org.alessio29.savagebot.commands.HelpCommand;
import org.alessio29.savagebot.commands.PingCommand;
import org.alessio29.savagebot.commands.cards.DealCardCommand;
import org.alessio29.savagebot.commands.cards.OpenCardCommand;
import org.alessio29.savagebot.commands.cards.ShowCardsCommand;
import org.alessio29.savagebot.commands.cards.ShuffleDeckCommand;
import org.alessio29.savagebot.commands.dice.RollDiceCommand;
import org.alessio29.savagebot.commands.dice.ShortRollDiceCommand;
import org.alessio29.savagebot.commands.initiative.DrawInitiativeCardCommand;
import org.alessio29.savagebot.commands.initiative.NewFightCommand;
import org.alessio29.savagebot.commands.initiative.NewRoundCommand;
import org.alessio29.savagebot.commands.initiative.ShowInitiativeCommand;

import com.Cardinal.CommandPackage.CommandClient;
import com.Cardinal.CommandPackage.Load.CommandLoader;
import com.Cardinal.CommandPackage.Proccessor.CommandRegistry;

public class SavageBotRunner {

	private static final String token = "INSERT_TOKEN_HERE!!!";
										 

	public static void main(String[] args) {
		CommandClient client = new CommandClient(token);
		client.addListener(new SavageBotListener(SavageBotListener.PREFIX));
		registerCommands();
		loadGames();
		
	}

	private static void loadGames() {
		// TODO this method should load stored games when bot starts
		
	}

	private static void registerCommands() {
		CommandRegistry.current().register(new PingCommand());
		CommandRegistry.current().register(new DrawInitiativeCardCommand());
		CommandRegistry.current().register(new ShowInitiativeCommand());
		CommandRegistry.current().register(new ShuffleDeckCommand());
//		CommandRegistry.current().register(new HatCommand());
//		CommandRegistry.current().register(new GetBennyCommand());
//		CommandRegistry.current().register(new ShowPocketCommand());
//		CommandRegistry.current().register(new UseBennyCommand());
		CommandRegistry.current().register(new NewFightCommand());
		CommandRegistry.current().register(new NewRoundCommand());
		CommandRegistry.current().register(new RollDiceCommand());
		CommandRegistry.current().register(new ShortRollDiceCommand());
		CommandRegistry.current().register(new DealCardCommand());
		CommandRegistry.current().register(new OpenCardCommand());
		CommandRegistry.current().register(new ShowCardsCommand());
		CommandRegistry.current().register(new HelpCommand());
		
		
		CommandLoader.current().loadCommands();
	}
}
