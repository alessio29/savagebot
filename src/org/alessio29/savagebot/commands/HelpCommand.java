package org.alessio29.savagebot.commands;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;
import com.Cardinal.CommandPackage.Exceptions.MissingRequirementsException;
import com.Cardinal.CommandPackage.Proccessor.CommandRegistry;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class HelpCommand implements ICommand {
	
	
	
	@Override
	public String getName() {
		return "help";
	}

	@Override
	public Category getCategory() {
		return Category.INFO;
	}

	@Override
	public String getDescription() {
		return "Lists the description and syntax for every registered command.";
	}

	@Override
	public String[] getArguments() {
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix)
			throws MissingRequirementsException, MissingArgumentsException {

		CategorizedMap categories = new CategorizedMap();
		CommandRegistry.current().getRegisteredCommands().stream().forEach(c -> {
			if (c.getCategory() != null) {
				categories.put(c.getCategory(), c);
			} else {
				categories.put(Category.MISC, c);
			}
		});
		
		categories.keySet().stream().sorted(Category::compare).forEach(c -> {
			StringBuilder b = new StringBuilder();
			b.append("\n--------------------------------------");
			categories.get(c).stream().forEach(com -> {
				b.append("\n" + ICommand.asHelpString(com, prefix) + "\n");
			});
			b.append("\n--------------------------------------");
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
			}
			event.getAuthor().getOrCreatePMChannel().sendMessage(c.toString() + " Commands"+b.toString());
		});
	}

	private class CategorizedMap extends HashMap<Category, HashSet<ICommand>> {

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
		public ICommand put(Category key, ICommand value) {
			if (get(key) == null) {
				super.put(key, new HashSet<ICommand>(Arrays.asList(new ICommand[] { value })));
			}
			get(key).add(value);
			return value;
		}

	}
	
	
	
// ====================================================================================
/*	
	

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix)
			throws MissingRequirementsException, MissingArgumentsException {
		
		IChannel channel = event.getChannel();
		String content = "List of commands: \n ping - checks SavageBot readiness \n help - shows this info\n"
				+ "deal [n] [user] - secretly deals n (1 by default) cards to user (to self by default) \n"
				+ "open [n] - openly deals n (1 by default) cards to current channel\n"
				+ "show - show your secret cards to current channel\n"
				+ "shuffle - shuffles deck, resets secret cards\n"
				+ "=====================================================\n"
				+ "roll or r = rolls dice\n"
				+ "Currently supported dice codes are:\n"
				+ "ndm - roll n m-sided dice, show sum\n"
				+ "ndm! - roll n 'exploding' m-sided dice, show sum\n"
				+ "ndmkp - roll n m-sided dice keep p highest\n"
				+ "ndmklp - roll n m-sided dice keep p lowest\\n"
				+ "sm - where m is one of (4,6,8,10,12) - Savage Worlds roll with wild die\n"
				+ "nsm - as previous but instead of d6 wild die is dn\n"
				+ "lxdice - roll dice l times - i.e. 6x4d6k3\n"
				+ "You can use multiple rolls in one command separeted by space:\n"
				+ "~r d6 d4! d10+d12\n"
				+ "You can use comments in roll:\n"
				+ "~r shooting at vampire s8 damage 2d6+1\n"
				+ "=====================================================\n"
				+ "fight - starts new fight: shuffles deck, resets intiative tracker\n"
				+ "round - starts new round: resets resets intiative tracker, shuffles deck, if joker was dealt on previous round\n"
				+ "show - shows initiative tracker\n"
				+ "draw character [ilq] - adds to initiative tracker character and draws card to him\n"
				+ "add q - if character has edge Quick, l - if character has edge Levelheaded and \n"
				+ "il - if character has edge Impreved Levelheaded";
		channel.sendMessage(content );
		
	}
*/
}
