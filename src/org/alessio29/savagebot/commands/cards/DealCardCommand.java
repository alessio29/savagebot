package org.alessio29.savagebot.commands.cards;

import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.internal.Users;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;
import com.Cardinal.CommandPackage.Exceptions.MissingRequirementsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class DealCardCommand implements ICommand {

	@Override
	public String getName() {
		return "deal";
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
		IGuild guild = event.getGuild();
		IUser user = event.getAuthor();
		if (args.length > 2) {
			IUser userParam = Users.findUser(args[2], guild);
			if (userParam == null ) {
				event.getChannel().sendMessage("Непонятно кому сдавать..");
				return;
			}
			user = userParam;
		}		
		String message = "";

		for (int i=0; i<count;i++) {
			Card newCard = deck.getCard();
			if (newCard!=null) {
				Hands.getHand(guild, user).getCards().add(newCard);
				message = message+newCard.toString()+" ";
			} else {
				break;
			}
		}
		IChannel ch = user.getOrCreatePMChannel();
		ch.sendMessage(message);
	}
}
