package org.alessio29.savagebot.internal.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.alessio29.savagebot.commands.IDiscordCommand;
import org.alessio29.savagebot.internal.IMessageReceived;

import java.lang.reflect.Method;

public class DiscordMethodSlashCommand implements IDiscordCommand {
    private final String name;
    private final Method method;
    private final Object methodOwner;
    private final boolean shouldDefer;
    private final String[] optionNames;

    public DiscordMethodSlashCommand(String name, Method method, Object methodOwner, boolean shouldDefer, String[] optionNames) {
        this.name = name;
        this.method = method;
        this.methodOwner = methodOwner;
        this.shouldDefer = shouldDefer;
        this.optionNames = optionNames;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean shouldDefer() {
        return shouldDefer;
    }

    @Override
    public String[] getOptionNames() {
        return optionNames;
    }

    @Override
    public CommandExecutionResult invoke(IMessageReceived<SlashCommandInteractionEvent> event, String[] optionValues) throws Exception {
        return (CommandExecutionResult) method.invoke(methodOwner, event, optionValues);
    }
}
