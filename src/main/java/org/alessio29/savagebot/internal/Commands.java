package org.alessio29.savagebot.internal;

import org.alessio29.savagebot.commands.admin.PingCommand;
import org.alessio29.savagebot.commands.admin.PrefixCommand;
import org.alessio29.savagebot.commands.bennies.GetBennyCommand;
import org.alessio29.savagebot.commands.bennies.HatCommand;
import org.alessio29.savagebot.commands.bennies.ShowPocketCommand;
import org.alessio29.savagebot.commands.bennies.UseBennyCommand;
import org.alessio29.savagebot.commands.cards.*;
import org.alessio29.savagebot.commands.dice.RollDice2Command;
import org.alessio29.savagebot.commands.dice.RollDiceCommand;
import org.alessio29.savagebot.commands.info.HelpCommand;
import org.alessio29.savagebot.commands.info.InviteCommand;
import org.alessio29.savagebot.commands.initiative.NewFightCommand;
import org.alessio29.savagebot.commands.initiative.NewRoundCommand;
import org.alessio29.savagebot.commands.initiative.ShowInitiativeCommand;

public class Commands {

	public static void registerDefaultCommands() {
		CommandRegistry registry = CommandRegistry.getInstance();
		
		// admin commands
		registry.registerCommand(new PingCommand());
		registry.registerCommand(new PrefixCommand());

		// info commands
		registry.registerCommand(new InviteCommand());
		registry.registerCommand(new HelpCommand());

		// bennies commands
		registry.registerCommand(new GetBennyCommand());
		registry.registerCommand(new HatCommand());
		registry.registerCommand(new ShowPocketCommand());
		registry.registerCommand(new UseBennyCommand());

		// cards commands
		registry.registerCommand(new DealCardCommand());
		registry.registerCommand(new DrawInitiativeCardCommand());
		registry.registerCommand(new OpenCardCommand());
		registry.registerCommand(new ShowCardsCommand());
		registry.registerCommand(new ShuffleDeckCommand());

		// dice commands
		registry.registerCommand(new RollDiceCommand());
		registry.registerCommand(new RollDice2Command());

		// initiative commands
		registry.registerCommand(new NewFightCommand());
		registry.registerCommand(new NewRoundCommand());
		registry.registerCommand(new ShowInitiativeCommand());
	}
}
