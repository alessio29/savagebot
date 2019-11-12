package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public interface ICommand {
	
	String getName();

	CommandCategory getCategory();
	
	String getDescription();
	
	String[] getAliases();
	
	String[] getArguments();
	
	CommandExecutionResult execute(IMessageReceived message, String[] args) throws Exception;

}
