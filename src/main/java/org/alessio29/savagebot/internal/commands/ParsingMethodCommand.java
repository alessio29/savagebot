package org.alessio29.savagebot.internal.commands;

import org.alessio29.savagebot.internal.IMessageReceived;

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
    public CommandExecutionResult parseAndExecuteOrNull(IMessageReceived message, String command) {
        try {
            return (CommandExecutionResult) method.invoke(methodOwner, message, command);
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
