package com.github.alessio29.savagebot.commands.bennies;



import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;
import com.github.alessio29.savagebot.internal.Messages;
import com.github.alessio29.savagebot.savagebot.bennies.BennyColor;
import com.github.alessio29.savagebot.savagebot.bennies.Pocket;
import com.github.alessio29.savagebot.savagebot.bennies.Pockets;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class UseBennyCommand implements ICommand {

	@Override
	public String getName() {
		return "use";
	}

	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.BENNIES;
	}

	@Override
	public String getDescription() {
		return "Uses one of character's benny";
	}

	@Override
	public String[] getArguments() {
		String[] res = {"<BennyColor>", "<CharacterName>"};
		return res;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		if(args.length <2) {
			throw new Exception("Cannot uderstand - must be 'use <BennyColor> <CharName>'.");
		}

		Guild guild = event.getGuild();
		Channel channel = event.getTextChannel();
		String charName = Messages.createNameFromArgs(args, 1);
		Pocket pocket = Pockets.getPocket(guild, channel, charName);
		BennyColor color = BennyColor.getColor(args[0]);
		if (color == null ) {
			throw new Exception("Something wrong with benny color.");
		}
		if (pocket.use(color)) {
			StringBuilder reply = new StringBuilder();
			reply.append(Messages.capitalize(charName)).append(" used ").append(color).append(" benny.\n");
			return new CommandExecutionResult(reply.toString(), 3);
		}
		return new CommandExecutionResult(charName+" has no such benny", 3);
	}

}
