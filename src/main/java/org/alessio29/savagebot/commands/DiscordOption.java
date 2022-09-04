package org.alessio29.savagebot.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public @interface DiscordOption {
    OptionType optionType() default OptionType.STRING;
    String name();
    String description();
    boolean isRequired() default false;
}
