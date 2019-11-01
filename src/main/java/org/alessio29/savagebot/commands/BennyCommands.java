package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.bennies.*;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.BENNIES)
public class BennyCommands {

    private static final String RESET = "fill";

    @CommandCallback(
            name = "hat",
            description = "Initializes bennies storage",
            aliases = {},
            arguments = {"[fill]"}
    )
    public static CommandExecutionResult init(MessageReceivedEvent event, String[] args) {
        boolean reset = (args.length>0) && args[0].equals(RESET);
        Hat hat = Hats.getHat(event.getGuild(), event.getTextChannel(), reset);
        if (reset) {
            Pockets.resetPockets(event.getGuild(), event.getTextChannel());
        }
        return new CommandExecutionResult(hat.getInfo(), ((reset)?2:1) );
    }

    @CommandCallback(
            name = "pocket",
            description = "Show character's bennies",
            aliases = {},
            arguments = {"<character_name>"}
    )
    public static CommandExecutionResult show(MessageReceivedEvent event, String[] args) {
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

    @CommandCallback(
            name = "use",
            description = "Give new benny to character",
            aliases = {},
            arguments = {"<benny_color>", "<character_name>"}
    )
    public static CommandExecutionResult take(MessageReceivedEvent event, String[] args) {
        if (args.length < 2) {
            return new CommandExecutionResult("Command syntax: use/take <BennyColor> <CharName>", args.length + 1);
        }

        Guild guild = event.getGuild();
        Channel channel = event.getTextChannel();
        String charName = ReplyBuilder.createNameFromArgs(args, 1);
        Pocket pocket = Pockets.getPocket(guild, channel, charName);
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


    @CommandCallback(
            name = "benny",
            description = "Give new benny to character",
            aliases = {},
            arguments = {"<character_name>"}
    )
    public static CommandExecutionResult give(MessageReceivedEvent event, String[] args) {
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
