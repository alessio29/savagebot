package org.alessio29.savagebot.commands.cards;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.commands.CommandCategory;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;



public class DealCardCommand implements ICommand {

	@Override
	public String getName() {
		return "deal";
	}

	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public CommandCategory getCategory() {
		
		return CommandCategory.CARDS;
	}

	@Override
	public String getDescription() {

		return "secretly deals n (1 by default) cards to user (to self by default)";
	}

	@Override
	public String[] getArguments() {
		return new String[]{"CardCount", "User"};
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
		Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());
		if(deck.isEmpty()) {
			return new CommandExecutionResult("Shuffle is needed..", 2);
		}
		int count = 1;
		if (args.length > 0) {
			try {
				count = Integer.parseInt(args[0]);	
			} catch (Exception e) {
				// count will be 1
			}
		}
		Guild guild = event.getGuild();
		User user = event.getAuthor();
		ReplyBuilder replyBuilder = new ReplyBuilder();
		for (int i=0; i<count;i++) {
			Card newCard = deck.getNextCard();
			if (newCard!=null) {
				Hands.getHand(guild, user).getCards().add(newCard);
				replyBuilder.attach(newCard.toString()).space();
			} else {
				break;
			}
		}
		return new CommandExecutionResult(replyBuilder.toString(), 2, true);
	}
}
