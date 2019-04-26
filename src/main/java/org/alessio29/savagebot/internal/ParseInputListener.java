package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.alessio29.savagebot.commands.ICommand;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;

public class ParseInputListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		if (event.getAuthor().isBot()) {
			// do not respond to bots (including myself :) ) 
			return;
		}
		String rawMessage = event.getMessage().getContentRaw();
		
		rawMessage = Messages.removeBlocks(rawMessage);
		rawMessage = Messages.removeQuotes(rawMessage);

		String[] words = rawMessage.split("\\s+");
		ArrayList<CommandExecutionResult> response = new ArrayList<>();   
		final User user = event.getAuthor();		
		boolean privateMessage = false; 
		boolean processed = false;
		for (int i = 0; i<words.length; i++) {
			words[i] = StringEscapeUtils.unescapeJava(words[i]);
		}
		int index = 0; 
		while (index < words.length) {
			String word = words[index];
			String prefix = Prefixes.getPrefix(user);
			if (word.trim().startsWith(prefix)) {
				String command = word.replaceFirst(prefix, "");
				
				ICommand cmd = CommandRegistry.current().getCommandByName(command);
				if (cmd!=null) {
					String[] args = new String[words.length-index-1];
					System.arraycopy(words, index+1, args, 0, words.length-index-1);
					try {
						CommandExecutionResult res = CommandInterpreter.runCommand(event, command, args);	
						processed = true;
						privateMessage = privateMessage | res.isPrivateMessage();
						response.add(res);
						index+=res.getToSkip();
					} catch (Exception e) {
						Messages.sendMessage(event.getAuthor(), event.getChannel(), "Error while executing command "+word+". Details:  "+e.getMessage(), false);
						index++;
					}					
				}
				if (!processed) {
					index ++;
					response.add(new CommandExecutionResult(word, 1));
				}

			} else {
				response.add(new CommandExecutionResult(word, 1));
				index++;
			}
		}
		MessageChannel channel = event.getChannel();

		boolean isPrivate = response.get(0).isPrivateMessage();
		String message = "";
		for (CommandExecutionResult res : response) {
			if (res.getResult().isEmpty()) {
				continue;
			}
			isPrivate = isPrivate && res.isPrivateMessage();
			message +=res.getResult()+" ";
		}
		if (processed) {
			Messages.sendMessage(event.getAuthor(), channel, message.trim(), isPrivate);
		}
	}
}