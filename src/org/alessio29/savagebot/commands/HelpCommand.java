package org.alessio29.savagebot.commands;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;
import com.Cardinal.CommandPackage.Exceptions.MissingRequirementsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public class HelpCommand implements ICommand{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "help";
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

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

}
