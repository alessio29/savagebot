package org.alessio29.savagebot.internal.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface IParsingCommand {

    CommandExecutionResult parseAndExecuteOrNull(MessageReceivedEvent event, String command);
}
