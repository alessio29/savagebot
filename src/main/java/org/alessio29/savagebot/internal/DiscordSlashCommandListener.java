package org.alessio29.savagebot.internal;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.alessio29.savagebot.commands.IDiscordCommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.commands.CommandRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DiscordSlashCommandListener extends ListenerAdapter {
    private final CommandRegistry commandRegistry = CommandRegistry.getInstance();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        IDiscordCommand discordCommand = commandRegistry.getDiscordCommand(commandName);
        if (discordCommand == null) {
            event.reply("Unrecognized command: '" + commandName + "'").queue();
            return;
        }
        if (discordCommand.shouldDefer()) {
            event.deferReply(true).queue();
            CommandExecutionResult result = invokeDiscordCommand(discordCommand, event);
            if (result.isPrivateMessage()) {
                // Use ephemeral message as a private reply.
                event.getHook().setEphemeral(true).sendMessage(result.getResult()).queue();
            } else {
                event.getHook().sendMessage(result.getResult()).queue();
            }
        } else {
            CommandExecutionResult result = invokeDiscordCommand(discordCommand, event);
            event.reply(result.getResult()).queue();
        }
    }

    private CommandExecutionResult invokeDiscordCommand(IDiscordCommand discordCommand, SlashCommandInteractionEvent event) {
        String[] optionValues = getOptionValues(event, discordCommand);
        try {
            return discordCommand.invoke(getMessageReceived(event), optionValues);
        } catch (Exception e) {
            return new CommandExecutionResult(discordCommand.getName() + " failed: " + e.getMessage());
        }
    }

    private IMessageReceived<SlashCommandInteractionEvent> getMessageReceived(final SlashCommandInteractionEvent event) {
        return new IMessageReceived<SlashCommandInteractionEvent>() {
            @Override
            public String getGuildId() {
                Guild guild = event.getGuild();
                return guild == null ? null : guild.getId();
            }

            @Override
            public String getChannelId() {
                return event.getChannel().getId();
            }

            @Override
            public String getAuthorId() {
                return event.getUser().getId();
            }

            @Override
            public String getAuthorMention() {
                return event.getUser().getAsMention();
            }

            @Override
            public String getRawMessage() {
                throw new UnsupportedOperationException("'getRawMessage()' unsupported for /-commands");
            }

            @Override
            public SlashCommandInteractionEvent getOriginalEvent() {
                return event;
            }

            @Override
            public List<String> getMentions() {
                throw new UnsupportedOperationException("'getMentions()' unsupported for /-commands");
            }
        };
    }

    private String[] getOptionValues(@NotNull SlashCommandInteractionEvent event, IDiscordCommand discordCommand) {
        String[] optionNames = discordCommand.getOptionNames();
        int numRegularOptions = optionNames.length;
        if (discordCommand.isVararg()) {
            numRegularOptions -= 1;
        }
        List<String> optionValues = new ArrayList<>();
        for (int i = 0; i < numRegularOptions; ++i) {
            OptionMapping om = event.getOption(optionNames[i]);
            if (om != null) {
                optionValues.add(om.getAsString());
            } else {
                optionValues.add(null);
            }
        }
        if (discordCommand.isVararg()) {
            OptionMapping varargOptionMapping = event.getOption(optionNames[optionNames.length - 1]);
            if (varargOptionMapping != null) {
                for (String word : varargOptionMapping.getAsString().split("\\s+")) {
                    if (word.length() > 0) {
                        optionValues.add(word);
                    }
                }
            }
        }
        return optionValues.toArray(new String[]{});
    }
}
