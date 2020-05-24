package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.bennies.BennyType;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.utils.ChannelConfig;
import org.alessio29.savagebot.internal.utils.ChannelConfigs;

public class SetBennyModeAction implements IBotAction {


    private static final String DEADLANDS_MODE = "deadlands";
    private static final String DEADLANDS_MODE_SHORT = "d";
    private static final String NORMAL_MODE = "normal";
    private static final String NORMAL_MODE_SHORT = "n";

    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Command syntax: setmode normal/deadlands", args.length + 1);
        }
        String mode = args[0].trim().toLowerCase();
        if (mode.equals(NORMAL_MODE) || mode.equals(NORMAL_MODE_SHORT) ) {
            ChannelConfig config = ChannelConfigs.getChannelConfig(message.getChannelId());
            config.setBennyType(BennyType.NORMAL);
            ChannelConfigs.save();
            return new CommandExecutionResult("Bennies set to normal", args.length + 1);
        }

        if (mode.equals(DEADLANDS_MODE) || mode.equals(DEADLANDS_MODE_SHORT)) {
            ChannelConfig config = ChannelConfigs.getChannelConfig(message.getChannelId());
            config.setBennyType(BennyType.DEADLANDS);
            ChannelConfigs.save();
            return new CommandExecutionResult("Bennies set to deadlands", args.length + 1);
        }

        return new CommandExecutionResult("mode must be either 'normal' ('n') or 'deadlands' ('d')", args.length + 1);
    }
}
