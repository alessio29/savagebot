package org.alessio29.savagebot.commands.admin;

import org.alessio29.savagebot.commands.Category;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.internal.CommandExecutionResult;
import org.alessio29.savagebot.internal.Prefixes;

public class PrefixCommand implements ICommand {

	@Override
	public String getName() {
		return "prefix";
	}

	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.ADMIN;
	}

	@Override
	public String getDescription() {
		return "Sets <character> as custom user-defined command prefix or shows current prefix";
	}

	@Override
	public String[] getArguments() {
		String[] res = {"[<character>]"}; 
		return res;
	}

	@Override
	public CommandExecutionResult execute(net.dv8tion.jda.core.events.message.MessageReceivedEvent event, String[] args) {
		
		CommandExecutionResult result = null;
		if (args.length>0) {
			String newPrefix = args[0].trim(); 
			if (newPrefix.length()>1) {
				result = new CommandExecutionResult("Prefix must be one-character long!", 1);
			} else {
				Prefixes.setPrefix(event.getAuthor(), newPrefix);
				result = new CommandExecutionResult("Prefix is set to "+newPrefix, 2);
			}
		} else {
			String prfx = Prefixes.getPrefix(event.getAuthor());
			if (prfx == null) {
				result = new CommandExecutionResult("Custom prefix is not set! Default prefix is "+Prefixes.DEFAULT_PREFIX, 1);
			} else {
				result = new CommandExecutionResult("Prefix is '"+prfx+"'", 1);
			}
		}
		return result;
	}
}
