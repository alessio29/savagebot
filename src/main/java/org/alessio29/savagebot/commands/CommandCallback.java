package org.alessio29.savagebot.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandCallback {
    String name();
    CommandCategory category() default CommandCategory.OTHER;
    String description();
    String[] aliases();
    String[] arguments();
}
