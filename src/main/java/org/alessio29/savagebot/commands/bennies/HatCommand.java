package org.alessio29.savagebot.commands.bennies;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.commands.Category;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.internal.CommandExecutionResult;
import org.alessio29.savagebot.savagebot.bennies.Hat;
import org.alessio29.savagebot.savagebot.bennies.Hats;
import org.alessio29.savagebot.savagebot.bennies.Pockets;



public class HatCommand implements ICommand {

	private static final String RESET = "fill";

	@Override
	public String getName() {
		return "hat";
	}

	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.BENNIES;
	}

	@Override
	public String getDescription() {
		return "Puts all required bennies into the hat";
	}

	@Override
	public String[] getArguments() {
		String[] res = {"[fill]"};
		return res;
	}


	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		
		boolean reset = (args.length>0) && args[0].equals(RESET);
		Hat hat = Hats.getHat(event.getGuild(), event.getTextChannel(), reset);
		if (reset) {
			Pockets.resetPockets(event.getGuild(), event.getTextChannel());
		}
		return new CommandExecutionResult(hat.getInfo(), ((reset)?2:1) );
	}
}
