package org.alessio29.savagebot.commands.cards;

import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;
import com.Cardinal.CommandPackage.Exceptions.MissingRequirementsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public class OpenCardCommand implements ICommand {

	@Override
	public String getName() {
		return "open";
	}

	@Override
	public Category getCategory() {
		
		return Category.CUSTOM;
	}

	@Override
	public String getDescription() {
		
		return "openly deals several (1 by default) cards to current channel\n";
	}

	@Override
	public String[] getArguments() {

		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix)
			throws MissingRequirementsException, MissingArgumentsException {

		Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());
		if(deck.isEmpty()) {
			event.getChannel().sendMessage("Перемешать бы надо..");
			return;
		}
		int count = 1;
		if (args.length > 1) {
			try {
				count = Integer.parseInt(args[1]);	
			} catch (Exception e) {
				// count will be 1
			}
		}
		String message = "";		
		for (int i=0; i<count;i++) {
			
			Card newCard = deck.getCard();
			if (newCard != null) {
				message = message+newCard.toString()+" ";
			} else {
				break;
			}
		}
		IChannel ch = event.getChannel();
		ch.sendMessage(message);
	}
}
