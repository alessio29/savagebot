package org.alessio29.savagebot.internal.commands;

import org.alessio29.savagebot.commands.CommandCategory;
import org.alessio29.savagebot.commands.ICommand;

public abstract class AbstractCommand implements ICommand {
    private final String name;
    private final CommandCategory category;
    private final String description;
    private final String[] aliases;
    private final String[] arguments;

    public AbstractCommand(
            String name,
            CommandCategory category,
            String description,
            String[] aliases,
            String[] arguments
    ) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.aliases = aliases;
        this.arguments = arguments;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CommandCategory getCategory() {
        return category;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public String[] getArguments() {
        return arguments;
    }
}
