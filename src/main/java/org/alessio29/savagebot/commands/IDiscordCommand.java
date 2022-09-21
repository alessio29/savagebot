package org.alessio29.savagebot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public interface IDiscordCommand {
    String getName();

    boolean shouldDefer();

    boolean isVararg();

    String[] getOptionNames();

    CommandExecutionResult invoke(IMessageReceived<SlashCommandInteractionEvent> event, String[] optionValues) throws Exception;
}
