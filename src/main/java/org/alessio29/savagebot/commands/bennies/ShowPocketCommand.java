package org.alessio29.savagebot.commands.bennies;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.commands.CommandCategory;
import org.alessio29.savagebot.internal.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.bennies.Pocket;
import org.alessio29.savagebot.bennies.Pockets;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;


public class ShowPocketCommand implements ICommand {

	@Override
	public String getName() {
		return "pocket";
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
		return "Shows character's bennies";
	}

	@Override
	public String[] getArguments() {
		String[] res = {"<characterName>"};
		return res;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		if( args.length<1) {
			throw new Exception("No character name provided. Usage: ~benny <Character Name>"); 
		}
		String charName = ReplyBuilder.createNameFromArgs(args, 0);
		Guild guild = event.getGuild();
		Channel channel = event.getTextChannel();
		Pocket pocket = Pockets.getPocket(guild, channel, charName);

		ReplyBuilder replyBuilder = new ReplyBuilder();
		replyBuilder.attach(ReplyBuilder.capitalize(charName)).
				attach(" has in his pocket").
				attach(pocket.getInfo()).newLine();
		return new CommandExecutionResult(replyBuilder.toString(), 2);
	}
}
