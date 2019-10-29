package org.alessio29.savagebot.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandCategoryOwner {
    CommandCategory value();
}
