package org.alessio29.savagebot.commands.cards;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Hand;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandCategory;
import org.alessio29.savagebot.internal.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;


public class ShowCardsCommand implements ICommand {

	@Override
	public String getName() {
		return "show";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.CARDS;
	}

	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public String getDescription() {
		return "Shows your cards, previously dealt to you by 'deal' command to current channel";
	}

	@Override
	public String[] getArguments() {
		return null;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		Hand hand = Hands.getHand(event.getGuild(), event.getAuthor());
		ReplyBuilder replyBuilder = new ReplyBuilder();
		for (Card card : hand.getCards()) {
			replyBuilder.space().attach(card.toString());
		}
		if(replyBuilder.toString().trim().isEmpty()) {
			return new CommandExecutionResult("Nothing to show..", 1);
		}
		return new CommandExecutionResult(replyBuilder.toString(), 1);
	}
}
