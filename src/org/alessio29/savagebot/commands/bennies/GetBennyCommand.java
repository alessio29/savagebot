package org.alessio29.savagebot.commands.bennies;

import org.alessio29.savagebot.bennies.Benny;
import org.alessio29.savagebot.bennies.Hat;
import org.alessio29.savagebot.bennies.Hats;
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

public class GetBennyCommand implements ICommand{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "benny";
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		return "Get benny from hat";
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix) {

		try {
			IGuild guild = event.getGuild();
			Hat hat = Hats.getHat(guild);
			IUser user = Users.findUser(args[1], guild);
			if (user == null ) {
				event.getChannel().sendMessage("Непонятно кто руки сует..");
				return;
			}

			Pocket pocket = Poсkets.getPocket(guild, user);
			Benny benny = hat.getBenny();
			if (benny == null ) {
				event.getChannel().sendMessage("Пустая шляпа-то..");
				return;
			}

			pocket.put(benny);
			StringBuilder reply = new StringBuilder();
			reply.append(user.mention()).append(" достал из шляпы ").append(benny.getColor()).append(" фишку.\n");
			IChannel ch = event.getChannel();
			ch.sendMessage(reply.toString());

		} catch (MissingArgumentsException e) {
			Messages.showError("Ничего не понимаю..", event);
		}

	}


}
