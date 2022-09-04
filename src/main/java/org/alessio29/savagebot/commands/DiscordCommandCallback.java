package org.alessio29.savagebot.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DiscordCommandCallback {
    String name();
    String description();
    DiscordOption[] options() default {};
    boolean shouldDefer() default false;
}
