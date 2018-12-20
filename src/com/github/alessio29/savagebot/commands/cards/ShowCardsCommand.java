package com.github.alessio29.savagebot.commands.cards;

import com.github.alessio29.savagebot.cards.Card;
import com.github.alessio29.savagebot.cards.Hand;
import com.github.alessio29.savagebot.cards.Hands;
import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class ShowCardsCommand implements ICommand {

	@Override
	public String getName() {
		return "show";
	}

	@Override
	public Category getCategory() {
		return Category.CARDS;
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
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
		String message = "";
		for (Card card : hand.getCards()) {
			message += " "+card.toString(); 
		}
		if(message.trim().isEmpty()) {
			message = "Nothing to show..";
		}
		return new CommandExecutionResult(message, 1);
	}
}
