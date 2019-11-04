package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.bennies.GiveBenniesAction;
import org.alessio29.savagebot.apiActions.bennies.InitBenniesAction;
import org.alessio29.savagebot.apiActions.bennies.ShowBenniesAction;
import org.alessio29.savagebot.apiActions.bennies.TakeBenniesAction;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.BENNIES)
public class BennyCommands {

    @CommandCallback(
            name = "hat",
            description = "Initializes bennies storage",
            aliases = {},
            arguments = {"[fill]"}
    )
    public static CommandExecutionResult init(MessageReceivedEvent event, String[] args) {
        return new InitBenniesAction().doAction(event.getGuild().getId(), event.getTextChannel().getId(), args);
    }

    @CommandCallback(
            name = "pocket",
            description = "Show character's bennies",
            aliases = {},
            arguments = {"<character_name>"}
    )
    public static CommandExecutionResult show(MessageReceivedEvent event, String[] args) {
        return new ShowBenniesAction().doAction(event.getGuild().getId(), event.getChannel().getId(), args);
    }

    @CommandCallback(
            name = "use",
            description = "Give new benny to character",
            aliases = {},
            arguments = {"<benny_color>", "<character_name>"}
    )
    public static CommandExecutionResult take(MessageReceivedEvent event, String[] args) {
        return new TakeBenniesAction().doAction(event.getGuild().getId(), event.getChannel().getId(), args);
    }


    @CommandCallback(
            name = "benny",
            description = "Give new benny to character",
            aliases = {},
            arguments = {"<character_name>"}
    )
    public static CommandExecutionResult give(MessageReceivedEvent event, String[] args) {
        return new GiveBenniesAction().doAction(event.getGuild().getId(), event.getChannel().getId(), args);
    }
}
