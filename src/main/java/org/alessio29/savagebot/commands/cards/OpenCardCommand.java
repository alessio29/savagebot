package org.alessio29.savagebot.commands.cards;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.commands.Category;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.internal.CommandExecutionResult;


public class OpenCardCommand implements ICommand {

	@Override
	public String getName() {
		return "open";
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Category getCategory() {
		
		return Category.CARDS;
	}

	@Override
	public String getDescription() {
		
		return "openly deals several (1 by default) cards to current channel";
	}

	@Override
	public String[] getArguments() {

		return null;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());
		if(deck.isEmpty()) {
			event.getChannel().sendMessage("Shuffle is needed..");
			return new CommandExecutionResult();
		}
		int count = 1;
		int index = 1;
		if (args.length > 0) {
			try {
				count = Integer.parseInt(args[0]);
				index = 2;
			} catch (Exception e) {
				// count will be 1
			}
		}
		String message = "";		
		for (int i=0; i<count;i++) {
			Card newCard = deck.getNextCard();
			if (newCard != null) {
				message = message+newCard.toString()+" ";
			} else {
				break;
			}
		}
		return new CommandExecutionResult(message, index);
	}
}