package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.bennies.Pocket;
import org.alessio29.savagebot.bennies.Pockets;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class TakeBenniesAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 2) {
            return new CommandExecutionResult("Command syntax: use/take <BennyColor> <CharName>", args.length + 1);
        }
        String charName = ReplyBuilder.createNameFromArgs(args, 1);
        Pocket pocket = Pockets.getPocket(message.getGuildId(), message.getChannelId(), charName);
        BennyColor color = BennyColor.getColor(args[0].trim());
        if (color == null) {
            return new CommandExecutionResult("Something wrong with benny color.", 3);
        }
        if (pocket.use(color)) {
            ReplyBuilder replyBuilder = new ReplyBuilder();
            replyBuilder.attach(ReplyBuilder.capitalize(charName)).
                    attach(" used ").
                    attach(ReplyBuilder.bold(color.toString())).
                    attach(" benny.").newLine();
            return new CommandExecutionResult(replyBuilder.toString(), 3);
        }
        return new CommandExecutionResult(charName + " has no such benny", 3);
    }
}
