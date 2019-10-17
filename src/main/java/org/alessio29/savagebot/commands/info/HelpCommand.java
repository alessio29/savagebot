package org.alessio29.savagebot.commands.info;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.commands.CommandCategory;
import org.alessio29.savagebot.internal.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.commands.CommandRegistry;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


public class HelpCommand implements ICommand {
	
	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public CommandCategory getCategory() {
		return CommandCategory.INFO;
	}

	@Override
	public String getDescription() {
		return "Lists the description and syntax for every registered command.";
	}

	@Override
	public String[] getArguments() {
		return null;
	}

	private class CategorizedMap extends HashMap<CommandCategory, HashSet<ICommand>> {

		private static final long serialVersionUID = 634791493137861944L;

		/**
		 * Associates the specified value with the specified key in this map. If the map
		 * previously contained a mapping for the key, the old value is replaced.
		 *
		 * @param key
		 *            key with which the specified value is to be associated
		 * @param value
		 *            value to be associated with the specified key
		 * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if
		 *         there was no mapping for <tt>key</tt>. (A <tt>null</tt> return can
		 *         also indicate that the map previously associated <tt>null</tt> with
		 *         <tt>key</tt>.)
		 */
		public ICommand put(CommandCategory key, ICommand value) {
			if (get(key) == null) {
				super.put(key, new HashSet<ICommand>(Arrays.asList(value)));
			}
			get(key).add(value);
			return value;
		}

	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
		CategorizedMap categories = new CategorizedMap();
		CommandRegistry.getInstance().getRegisteredCommands().stream().forEach(c -> {
			if (c.getCategory() != null) {
				categories.put(c.getCategory(), c);
			} else {
				categories.put(CommandCategory.OTHER, c);
			}
		});
		
		ReplyBuilder replyBuilder = new ReplyBuilder();


		categories.keySet().stream().sorted(CommandCategory::compareTo).forEach(category -> {
			replyBuilder.newLine().attach(ReplyBuilder.underlined(ReplyBuilder.bold(category.toString()+" category"))).newLine();
			categories.get(category).stream().forEach(command -> {
				replyBuilder.newLine().attach(command.asHelpString()).newLine();
			});
		});
		return new CommandExecutionResult(replyBuilder.toString(), 1, true);
	}
	

}
