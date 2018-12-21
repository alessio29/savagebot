package com.github.alessio29.savagebot.commands;

import com.github.alessio29.savagebot.internal.CommandExecutionResult;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class SetVariableCommand implements ICommand{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "set";
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public void execute(MessageReceivedEvent event, String[] args, String prefix)
//			throws MissingRequirementsException, MissingArgumentsException {
//		
//		IUser user = event.getAuthor();
//		IGuild guild = event.getGuild();
//		String[] val = args[1].split("=");
//		String name = val[0].substring(1);
//		String value = val[1];
//		Variables.addVariable(guild, user, name, value);
//	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}
}
