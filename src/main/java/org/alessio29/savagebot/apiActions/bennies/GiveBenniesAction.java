package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.bennies.Benny;
import org.alessio29.savagebot.bennies.Hat;
import org.alessio29.savagebot.bennies.Hats;
import org.alessio29.savagebot.bennies.Pocket;
import org.alessio29.savagebot.bennies.Pockets;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class GiveBenniesAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("No character name provided. Usage: !give <character_name>", 1);
        }

        Hat hat = Hats.getHat(message.getGuildId(), message.getChannelId(), false);
        String charName = ReplyBuilder.createNameFromArgs(args, 0);
        Pocket pocket = Pockets.getPocket(message.getGuildId(), message.getChannelId(), charName);
        Benny benny = hat.getBenny();
        if (benny == null) {
            return new CommandExecutionResult("Hat is empty..", 2);
        }
        pocket.put(benny);

        ReplyBuilder replyBuilder = new ReplyBuilder();
        replyBuilder.attach(" got from hat ").
                attach(ReplyBuilder.bold(benny.getColor().toString())).
                attach(" benny for ").
                attach(ReplyBuilder.bold(
                        ReplyBuilder.capitalize(charName)
                )).newLine();
        return new CommandExecutionResult(replyBuilder.toString(), 2);
    }
}
