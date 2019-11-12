package org.alessio29.savagebot.internal.commands;

import org.alessio29.savagebot.internal.IMessageReceived;

public interface IParsingCommand {

    CommandExecutionResult parseAndExecuteOrNull(IMessageReceived message, String command);
}
