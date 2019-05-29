package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.CommandExecutionResult;

public interface IParsingCommand {

    CommandExecutionResult parseAndExecuteOrNull(MessageReceivedEvent event, String command);

}
