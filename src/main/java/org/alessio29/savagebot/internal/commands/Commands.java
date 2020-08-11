package org.alessio29.savagebot.internal.commands;

import org.alessio29.savagebot.commands.*;

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
		registry.registerCommandsFromStaticMethods(CardCommands.class);

		// dice commands
		registry.registerCommandsFromStaticMethods(DiceCommands.class);

		// initiative commands
		registry.registerCommandsFromStaticMethods(InitCommands.class);

		// tokens commands
		registry.registerCommandsFromStaticMethods(TokenCommands.class);

		// states commands
		registry.registerCommandsFromStaticMethods(StatesCommands.class);

		// characters commands
		registry.registerCommandsFromStaticMethods(CharacterCommands.class);
	}
}
