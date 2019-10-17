package org.alessio29.savagebot.commands.bennies;


import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.commands.CommandCategory;
import org.alessio29.savagebot.internal.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.bennies.Pocket;
import org.alessio29.savagebot.bennies.Pockets;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;


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
	public CommandCategory getCategory() {
		return CommandCategory.BENNIES;
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
		String charName = ReplyBuilder.createNameFromArgs(args, 1);
		Pocket pocket = Pockets.getPocket(guild, channel, charName);
		BennyColor color = BennyColor.getColor(args[0].trim());
		if (color == null ) {
			throw new Exception("Something wrong with benny color.");
		}
		if (pocket.use(color)) {
			ReplyBuilder replyBuilder = new ReplyBuilder();
			replyBuilder.attach(ReplyBuilder.capitalize(charName)).
					attach(" used ").
					attach(ReplyBuilder.bold(color.toString())).
					attach(" benny.").newLine();
			return new CommandExecutionResult(replyBuilder.toString(), 3);
		}
		return new CommandExecutionResult(charName+" has no such benny", 3);
	}

}
