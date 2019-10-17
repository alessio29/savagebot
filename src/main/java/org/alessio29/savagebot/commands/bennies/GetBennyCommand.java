package org.alessio29.savagebot.commands.bennies;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.commands.CommandCategory;
import org.alessio29.savagebot.internal.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.bennies.*;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;


public class GetBennyCommand implements ICommand{

	@Override
	public String getName() {
		return "benny";
	}

	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public CommandCategory getCategory() {
		return CommandCategory.BENNIES;
	}

	@Override
	public String getDescription() {
		return "Get benny from hat and adds it to characker's pocket";
	}

	@Override
	public String[] getArguments() {
		return new String[]{"<character>"};
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		if(args.length<1) {
			throw new Exception("No character name provided. Usage: ~benny <Character>");
		}
		
		Guild guild = event.getGuild();
		Channel channel = event.getTextChannel();
		Hat hat = Hats.getHat(guild, channel, false);
		String charName = ReplyBuilder.createNameFromArgs(args, 0);
		Pocket pocket = Pockets.getPocket(guild, channel, charName);
		Benny benny = hat.getBenny();
		if (benny == null ) {
			return new CommandExecutionResult("Hat is empty..", 2);
		}
		pocket.put(benny);

		ReplyBuilder replyBuilder = new ReplyBuilder();
		replyBuilder.attach(" got from hat ").
				attach(ReplyBuilder.bold(benny.getColor().toString())).
				attach(" benny for ").
				attach(ReplyBuilder.bold(
						ReplyBuilder.capitalize(charName)
				)).newLine();
		return new CommandExecutionResult(replyBuilder.toString(), 2);
	}


}
