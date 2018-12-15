package org.alessio29.savagebot.commands.cards;

import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Hand;
import org.alessio29.savagebot.cards.Hands;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;
import com.Cardinal.CommandPackage.Exceptions.MissingRequirementsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public class ShowCardsCommand implements ICommand {

	@Override
	public String getName() {
		return "show";
	}

	@Override
	public Category getCategory() {
		
		return Category.CUSTOM;
	}

	@Override
	public String getDescription() {

		return "Shows your cards, previously dealt to you by 'deal' command to current channel\\n";
	}

	@Override
	public String[] getArguments() {
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix)
			throws MissingRequirementsException, MissingArgumentsException {
		
		Hand hand = Hands.getHand(event.getGuild(), event.getAuthor());
		String message = "";
		for (Card card : hand.getCards()) {
			message += " "+card.toString(); 
		}
		IChannel ch = event.getChannel();
		if(message.trim().isEmpty()) {
			message = "Показывать нечего..";
		}
		ch.sendMessage(message);
	}
}
