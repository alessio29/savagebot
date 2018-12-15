package org.alessio29.savagebot.commands.cards;

import java.util.List;

import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.cards.Hands;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class ShuffleDeckCommand implements ICommand {

	@Override
	public String getName() {

		return "shuffle";
	}

	@Override
	public Category getCategory() {
		
		return Category.CUSTOM;
	}

	@Override
	public String getDescription() {
		return "Shuffles current deck, resets secret cards dealt to all users in this channel\\n";
	}

	@Override
	public String[] getArguments() {

		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix) {

		Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());
		deck.shuffle();	
		IGuild guild = event.getGuild();
		List<IUser> users = event.getChannel().getUsersHere();
		for (IUser user : users) {
			Hands.getHand(guild, user).clear();
		}
		event.getChannel().sendMessage("Помешано...");
	}


}
