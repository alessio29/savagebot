package org.alessio29.savagebot.apiActions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public interface IDiscordAction {

    CommandExecutionResult doAction(MessageReceivedEvent event, String[] args);
}
