package org.alessio29.savagebot.internal.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ParsingMethodCommand implements IParsingCommand {
    private final Object methodOwner;
    private final Method method;

    public ParsingMethodCommand(Object methodOwner, Method method) {
        this.methodOwner = methodOwner;
        this.method = method;
    }

    @Override
    public CommandExecutionResult parseAndExecuteOrNull(MessageReceivedEvent event, String command) {
        try {
            return (CommandExecutionResult) method.invoke(methodOwner, event, command);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{ method: " + method.getDeclaringClass().getName() + "::" + method.getName() +
                "; methodOwner: " + methodOwner +
                "}";
    }
}
