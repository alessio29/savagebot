package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.alessio29.savagebot.internal.builders.DiscordResponseBuilder;
import org.alessio29.savagebot.internal.commands.CommandInterpreter;

public class ParseInputListener extends ListenerAdapter {

    @Override
	public void onMessageReceived(MessageReceivedEvent event) {
		User author = event.getAuthor();
		MessageChannel channel = event.getChannel();
		if (author.isBot()) {
			// do not respond to bots (including myself :) ) 
			return;
		}
        DiscordResponseBuilder responseBuilder = new DiscordResponseBuilder(author, channel);
		String rawMessage = event.getMessage().getContentRaw();
        String prefix = Prefixes.getPrefix(author.getId());
        new CommandInterpreter(prefix).run(rawMessage, responseBuilder, event);
		responseBuilder.sendResponse();
	}
}