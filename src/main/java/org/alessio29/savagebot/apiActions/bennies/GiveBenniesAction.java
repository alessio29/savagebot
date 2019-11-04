package org.alessio29.savagebot.apiActions.bennies;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.bennies.*;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class GiveBenniesAction implements IDiscordAction {
    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("No character name provided. Usage: !give <character_name>", 1);
        }

        Guild guild = event.getGuild();
        Channel channel = event.getTextChannel();
        Hat hat = Hats.getHat(guild, channel, false);
        String charName = ReplyBuilder.createNameFromArgs(args, 0);
        Pocket pocket = Pockets.getPocket(guild, channel, charName);
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
