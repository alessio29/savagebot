package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.commands.IParsingCommand;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Arrays;

public class ParseInputListener extends ListenerAdapter {

	private static class ResponseBuilder {
		private boolean hasCommandResult = false;
		private final StringBuilder publicPart = new StringBuilder();
		private final StringBuilder privatePart = new StringBuilder();

		void addRaw(String string) {
			publicPart.append(string).append(" ");
		}

		void addResult(CommandExecutionResult result) {
			if (result.isPrivateMessage()) {
				privatePart.append(result.getResult()).append(" ");
			} else  {
				hasCommandResult = true;
				publicPart.append(result.getResult()).append(" ");
			}
		}

		void sendResponse(User user, MessageChannel channel) {
			String privatePart = this.privatePart.toString().trim();
			String publicPart = this.publicPart.toString().trim();
			if (publicPart.length() > 0 && hasCommandResult) {
				Messages.sendMessage(user, channel, publicPart, false);
			}
			if (privatePart.length() > 0) {
				Messages.sendMessage(user, channel, privatePart, true);
			}
		}
	}

	private final CommandRegistry registry = CommandRegistry.getInstance();

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		User author = event.getAuthor();
		MessageChannel channel = event.getChannel();

		if (author.isBot()) {
			// do not respond to bots (including myself :) ) 
			return;
		}
		String rawMessage = event.getMessage().getContentRaw();

		rawMessage = Messages.removeBlocks(rawMessage);
		rawMessage = Messages.removeQuotes(rawMessage);

		String[] words = rawMessage.split("\\s+");

		for (int i = 0; i < words.length; i++) {
			words[i] = StringEscapeUtils.unescapeJava(words[i]);
		}

		ResponseBuilder responseBuilder = new ResponseBuilder();

		int index = 0;
		while (index < words.length) {
			String word = words[index];
			String prefix = Prefixes.getPrefix(author);

			boolean isCommand = false;

			if (word.trim().startsWith(prefix)) {
				String command = word.replaceFirst(prefix, "");

				ICommand cmd = registry.getCommandByName(command);
				if (cmd != null) {
					isCommand = true;
					String[] args = Arrays.copyOfRange(words, index + 1, words.length);
					try {
						CommandExecutionResult res = cmd.execute(event, args);
						responseBuilder.addResult(res);
						index += res.getToSkip();
					} catch (Exception e) {
						reportError(author, channel, word, e);
						index++;
					}
				} else {
					for (IParsingCommand pcmd : registry.getRegisteredParsingCommands()) {
						try {
							CommandExecutionResult res = pcmd.parseAndExecuteOrNull(event, word);
							if (res != null) {
								isCommand = true;
								responseBuilder.addResult(res);
								break;
							}
						} catch (Exception e) {
							reportError(author, channel, word, e);
							index++;
						}
					}
				}
			}

			if (!isCommand) {
				responseBuilder.addRaw(word);
				index++;
			}
		}

		responseBuilder.sendResponse(author, channel);
	}

	private void reportError(User author, MessageChannel channel, String word, Exception e) {
		Messages.sendMessage(author, channel, "Error while executing command " + word + ". Details: " + e.getMessage(), false);
	}
}