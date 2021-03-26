package org.alessio29.savagebot.apiActions.admin;

import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.Prefixes;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

public class PrefixAction {

    private static final List<String> restrictedPrefixes;

    static {
        restrictedPrefixes = Arrays.asList("?", "*", "^", "$", "\\", "+", "(", ")");
    }

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length <= 0) {
            String prefix = Prefixes.getPrefix(message.getAuthorId());
            if (prefix == null) {
                return new CommandExecutionResult("Custom prefix is not set! Default prefix is " + Prefixes.DEFAULT_BOT_PREFIX, 1);
            } else {
                return new CommandExecutionResult("Prefix is '" + prefix + "'", 1);
            }
        }
        String newPrefix = args[0].trim();

        if (newPrefix.equalsIgnoreCase("reset")) {
            Prefixes.resetPrefix(message.getAuthorId());
            return new CommandExecutionResult("Prefix is reset to default", 2);
        }
        if (newPrefix.length() > 1) {
            return new CommandExecutionResult("Prefix must be one-character long!", 1);
        }
        boolean isRestricted = false;
        for (String restrictedPrefix : restrictedPrefixes) {
            isRestricted = isRestricted || newPrefix.equals(restrictedPrefix);
        }
        if (isRestricted) {
            return new CommandExecutionResult("Prefix cannot be set to following symbols: "+ StringUtils.join(restrictedPrefixes, ", "), 1);
        }
        Prefixes.setPrefix(message.getAuthorId(), newPrefix);
        return new CommandExecutionResult("Prefix is set to " + newPrefix, 2);
    }
}
