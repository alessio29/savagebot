package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.admin.InfoAction;
import org.alessio29.savagebot.apiActions.admin.PingAction;
import org.alessio29.savagebot.apiActions.admin.PrefixAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.ADMIN)
public class AdminCommands {

    @CommandCallback(
            name = "info",
            description = "Shows bot stats",
            aliases = {},
            arguments = {"password"}
    )
    public static CommandExecutionResult info(IMessageReceived<MessageReceivedEvent> message, String[] args) {
        return new InfoAction().doAction(message, args);
    }

    @CommandCallback(
            name = "ping",
            description = "Checks SavageBot readiness",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult ping(IMessageReceived message, String[] args) {
        return new PingAction().doAction(message, args);
    }

    @CommandCallback(
            name = "prefix",
            description = "Sets <character> as custom user-defined command prefix or shows current prefix",
            aliases = {},
            arguments = {"[<character>]"}
    )
    public static CommandExecutionResult prefix(IMessageReceived message, String[] args) {
        return new PrefixAction().doAction(message, args);
    }
}
