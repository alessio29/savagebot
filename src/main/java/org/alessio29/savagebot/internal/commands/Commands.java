package org.alessio29.savagebot.internal.commands;

import org.alessio29.savagebot.commands.DiceCommands;
import org.alessio29.savagebot.commands.InfoCommands;
import org.alessio29.savagebot.commands.InitCommands;
import org.alessio29.savagebot.commands.TokenCommands;
import org.alessio29.savagebot.commands.AdminCommands;
import org.alessio29.savagebot.commands.BennyCommands;
import org.alessio29.savagebot.commands.cards.*;

public class Commands {

	public static void registerDefaultCommands() {
		CommandRegistry registry = CommandRegistry.getInstance();
		
		// admin commands
		registry.registerCommandsFromStaticMethods(AdminCommands.class);

		// info commands
		registry.registerCommandsFromStaticMethods(InfoCommands.class);

		// bennies commands
		registry.registerCommandsFromStaticMethods(BennyCommands.class);

		// cards commands
		registry.registerCommand(new DealCardCommand());
		registry.registerCommand(new DrawInitiativeCardCommand());
		registry.registerCommand(new OpenCardCommand());
		registry.registerCommand(new ShowCardsCommand());
		registry.registerCommand(new ShuffleDeckCommand());

		// dice commands
		registry.registerCommandsFromStaticMethods(DiceCommands.class);

		// initiative commands
		registry.registerCommandsFromStaticMethods(InitCommands.class);

		// tokens commands
		registry.registerCommandsFromStaticMethods(TokenCommands.class);

	}
}
