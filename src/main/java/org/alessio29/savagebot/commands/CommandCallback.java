package org.alessio29.savagebot.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandCallback {
    String name();
    CommandCategory category() default CommandCategory.OTHER;
    String description();
    String[] aliases();
    String[] arguments();
}
