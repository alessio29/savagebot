package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.SavageBotRunner;
import org.alessio29.savagebot.internal.Prefixes;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.ADMIN)
public class AdminCommands {

    @CommandCallback(
            name = "info",
            description = "Shows bot stats",
            aliases = {},
            arguments = {"password"}
    )
    public static CommandExecutionResult info(MessageReceivedEvent event, String[] args) {
        CommandExecutionResult result = null;
        if (args.length>0) {
            String password = args[0].trim();
            if (SavageBotRunner.passwdOk(password)) {
                int serversCount = event.getJDA().getGuilds().size();
                result = new CommandExecutionResult("Bot registered at "+serversCount + " servers.", 2);
            }
        } else {
            result = new CommandExecutionResult("Password must be provided!", 1);
        }
        return result;
    }

    @CommandCallback(
            name = "ping",
            description = "Checks SavageBot readiness",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult ping(MessageReceivedEvent event, String[] args) {
        return new CommandExecutionResult(
                new ReplyBuilder().attach("Hey, ").
                        attach(ReplyBuilder.mention(event.getAuthor())).
                        attach(" SavageBot is ready!").newLine().toString(),
                1);
    }

    @CommandCallback(
            name = "prefix",
            description = "Sets <character> as custom user-defined command prefix or shows current prefix",
            aliases = {},
            arguments = {"[<character>]"}
    )
    public static CommandExecutionResult prefix(MessageReceivedEvent event, String[] args) {

        CommandExecutionResult result;
        if (args.length>0) {
            String newPrefix = args[0].trim();
            if (newPrefix.length()>1) {
                result = new CommandExecutionResult("Prefix must be one-character long!", 1);
            } else {

                if (newPrefix.equals("?") || newPrefix.equals("*") || newPrefix.equals("^") || newPrefix.equals("\\")  ) {
                    result = new CommandExecutionResult("Prefix must not be question sign, asterisk, backslash or circumflex!", 1);
                } else {
                    Prefixes.setPrefix(event.getAuthor(), newPrefix);
                    result = new CommandExecutionResult("Prefix is set to "+newPrefix, 2);
                }
            }
        } else {
            String prfx = Prefixes.getPrefix(event.getAuthor());
            if (prfx == null) {
                result = new CommandExecutionResult("Custom prefix is not set! Default prefix is "+Prefixes.DEFAULT_BOT_PREFIX, 1);
            } else {
                result = new CommandExecutionResult("Prefix is '"+prfx+"'", 1);
            }
        }
        return result;
    }
}
