package org.alessio29.savagebot.apiActions;

import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public interface IBotAction {

    CommandExecutionResult doAction(IMessageReceived message, String[] args);

}
