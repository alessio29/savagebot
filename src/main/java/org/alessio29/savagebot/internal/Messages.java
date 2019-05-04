package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.characters.CharacterInitiative;
import org.alessio29.savagebot.dice.DiceRollResult;
import org.alessio29.savagebot.dice.DicelessRollResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Messages {

    public static final String SUCCESS_MARK = ":white_check_mark:";
    public static final String FAIL_MARK = ":red_circle:";

    public static void sendMessage(User user, MessageChannel messageChannel, String message, boolean isPrivate) {
		if (isPrivate) {
			user.openPrivateChannel().queue((channel) ->
			{
				channel.sendMessage(message).queue();
			});
				
		} else {
			messageChannel.sendMessage(user.getAsMention()+" "+message).queue();
		}
	}

	public static String mention(User user) {
		return user.getAsMention();
	}

	public static String bold(String message) {
		return "**"+message+"**";
	}
	
	public static String italic(String message) {
		return "*"+message+"*";
	}	
	
	public static String underlined(String message) {
		return "__"+message+"__";
	}	

	public static String strikeout(String message) {
		return "~~"+message+"~~";
	}	

	public static String capitalize(String charName) {
		return charName.substring(0, 1).toUpperCase()+charName.substring(1);
	}

	public static String buildResponse(ArrayList<String> response) {
		
		return String.join(" ", response);
	}

	public static String createNameFromArgs(String[] args, int startFrom) {
		String charName="";
		for (int i=startFrom; i<args.length;i++) {
			 charName+=" "+args[i];
		}
		return charName.trim();
	}

	public static String showOrder(MessageReceivedEvent event) {
		
		StringBuilder reply = new StringBuilder();
		Set<CharacterInitiative> chars = CharacterInitCache.getCharacters(event.getGuild());
		if (chars!=null && !chars.isEmpty()) {
			for (CharacterInitiative c : CharacterInitCache.getCharacters(event.getGuild())) {
				String allCards = c.getAllCards().stream().map(cr ->cr.toString()).collect(Collectors.joining(","));
				reply.append(c.getName()).append(" - ").append(" - ").append(c.getBestCard().toString()).append("["+allCards+"]").append("\n");
			}
		} else {
			reply.append("No cards dealt!");
		}
		
		return reply.toString();
	}

	public static String createStringRepresentation(List<DiceRollResult> rollResults) {
		String result = "";
		int num = 1;
		for (DiceRollResult roll : rollResults) {
			String s = toStr(roll);
			if (rollResults.size()>1) {
				s = num+": "+s;
			}
			if (roll instanceof DicelessRollResult) {
				result = result.concat(s);
			} else {
				result = result.concat(s+"\n");	
			}
			num++;
		}
		return result;
	}
	
	private static String toStr(DiceRollResult roll) {
		
		if (!roll.getOriginal().trim().isEmpty()) {
			return roll.getOriginal()+" ";
		}
		String dieCode="";
		if (!roll.getDieCode().trim().isEmpty()) {
			dieCode=roll.getDieCode()+"=";
		}
		String details="";
		if (!roll.getDetails().trim().isEmpty() && (roll.getDetails().contains("+") || (roll.getDetails().contains("-") || roll.getDetails().contains(";") ))) {
			details=roll.getDetails()+"=";
		} 
		return dieCode.concat(details).concat((roll.getResult()+" "));
	}

	public static List<String> bold(String[] list) {
		
		List<String> res = new ArrayList<String>();
		for (String s : list) {
			res.add(bold(s));
		}
		return res;
	}

	public static final String BLOCK_MARKER = "```";
	private static final String QUOTE_MARKER = ">"; 
	
	
	public static String removeBlocks(String rawMessage) {

		while (rawMessage.contains(BLOCK_MARKER)) {
			int blockStarts = rawMessage.indexOf(BLOCK_MARKER);
			int blockStops = rawMessage.indexOf(BLOCK_MARKER, blockStarts+1);
			if (blockStops==-1) {
				return rawMessage;
			}
			rawMessage = rawMessage.substring(0, blockStarts)+rawMessage.substring(blockStops+3);
		}
		return rawMessage;
	}

	public static String removeQuotes(String rawMessage) {
		
		String[] lines = rawMessage.split("\n"); 
		ArrayList<String> res = new ArrayList<>();
		
		for (String line : lines ) { 
			if (!line.startsWith(QUOTE_MARKER)) {
				res.add(line);
			}
		}
		return String.join("\n", res);
	}
}
