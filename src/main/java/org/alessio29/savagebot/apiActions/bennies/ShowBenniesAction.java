package org.alessio29.savagebot.apiActions.bennies;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.bennies.Pocket;
import org.alessio29.savagebot.bennies.Pockets;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class ShowBenniesAction implements IDiscordAction {
    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("No character name provided. Usage: !show/benny <character_name>", 1);
        }
        String charName = ReplyBuilder.createNameFromArgs(args, 0);
        Guild guild = event.getGuild();
        Channel channel = event.getTextChannel();
        Pocket pocket = Pockets.getPocket(guild, channel, charName);

        ReplyBuilder replyBuilder = new ReplyBuilder();
        replyBuilder.attach(ReplyBuilder.capitalize(charName)).
                attach(" has in his pocket").
                attach(pocket.getInfo()).newLine();
        return new CommandExecutionResult(replyBuilder.toString(), 2);
    }
}
