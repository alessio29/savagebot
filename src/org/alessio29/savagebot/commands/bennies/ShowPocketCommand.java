package org.alessio29.savagebot.commands.bennies;

import org.alessio29.savagebot.bennies.Pocket;
import org.alessio29.savagebot.bennies.Poсkets;
import org.alessio29.savagebot.internal.Messages;
import org.alessio29.savagebot.internal.Users;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class ShowPocketCommand implements ICommand {

	@Override
	public String getName() {
		return "pocket";
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		return "Показывает что у него в карманцах...";
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix){

		try {
			IGuild guild = event.getGuild();
			IUser user = Users.findUser(args[1], guild);
			if (user == null ) {
				event.getChannel().sendMessage("Непонятно кто руки сует..");
				return;
			}
			Pocket pocket = Poсkets.getPocket(guild, user);
			StringBuilder reply = new StringBuilder();
			reply.append("В карманцах у ").append(user.mention()).append(pocket.getInfo()).append("\n");
			IChannel ch = event.getChannel();
			ch.sendMessage(reply.toString());
		} catch (MissingArgumentsException e) {
			Messages.showError("Ничего не понимаю..", event);
		}

	}

}
