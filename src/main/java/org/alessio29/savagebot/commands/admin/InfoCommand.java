package org.alessio29.savagebot.commands.admin;

import org.alessio29.savagebot.SavageBotRunner;
import org.alessio29.savagebot.internal.commands.CommandCategory;
import org.alessio29.savagebot.internal.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class InfoCommand implements ICommand {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.ADMIN;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String[] getArguments() {
        String[] res = {"[<character>]"};
        return res;
    }

    @Override
    public CommandExecutionResult execute(net.dv8tion.jda.core.events.message.MessageReceivedEvent event, String[] args) {

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
}
