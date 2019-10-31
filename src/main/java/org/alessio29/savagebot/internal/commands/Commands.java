package org.alessio29.savagebot.internal.commands;

import org.alessio29.savagebot.commands.admin.PingCommand;
import org.alessio29.savagebot.commands.admin.PrefixCommand;
import org.alessio29.savagebot.commands.bennies.GetBennyCommand;
import org.alessio29.savagebot.commands.bennies.HatCommand;
import org.alessio29.savagebot.commands.bennies.ShowPocketCommand;
import org.alessio29.savagebot.commands.bennies.UseBennyCommand;
import org.alessio29.savagebot.commands.cards.*;
<<<<<<< HEAD
import org.alessio29.savagebot.commands.dice.RollDiceCommand;
import org.alessio29.savagebot.commands.dice.RollSortedCommand;
import org.alessio29.savagebot.commands.info.HelpCommand;
import org.alessio29.savagebot.commands.info.InviteCommand;
=======
import org.alessio29.savagebot.commands.dice.DiceCommands;
import org.alessio29.savagebot.commands.info.InfoCommands;
>>>>>>> 1cd41345815af0f3a535fa0845dbac1d09d2cd3e
import org.alessio29.savagebot.commands.initiative.NewFightCommand;
import org.alessio29.savagebot.commands.initiative.NewRoundCommand;
import org.alessio29.savagebot.commands.initiative.ShowInitiativeCommand;
import org.alessio29.savagebot.commands.tokens.ClearTokensCommand;
import org.alessio29.savagebot.commands.tokens.GiveTokensCommand;
import org.alessio29.savagebot.commands.tokens.ListTokensCommand;
import org.alessio29.savagebot.commands.tokens.TakeTokensCommand;

public class Commands {

	public static void registerDefaultCommands() {
		CommandRegistry registry = CommandRegistry.getInstance();
		
		// admin commands
		registry.registerCommand(new PingCommand());
		registry.registerCommand(new PrefixCommand());

		// info commands
		registry.registerCommandsFromStaticMethods(InfoCommands.class);

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
<<<<<<< HEAD
		registry.registerCommand(new RollDiceCommand());
		registry.registerCommand(new RollSortedCommand());
=======
		registry.registerCommandsFromStaticMethods(DiceCommands.class);
>>>>>>> 1cd41345815af0f3a535fa0845dbac1d09d2cd3e

		// initiative commands
		registry.registerCommand(new NewFightCommand());
		registry.registerCommand(new NewRoundCommand());
		registry.registerCommand(new ShowInitiativeCommand());

		// tokens commands
		registry.registerCommand(new ListTokensCommand());
		registry.registerCommand(new GiveTokensCommand());
		registry.registerCommand(new TakeTokensCommand());
		registry.registerCommand(new ClearTokensCommand());

	}
}
