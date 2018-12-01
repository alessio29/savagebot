package org.alessio29.savagebot.commands.bennies;

import org.alessio29.savagebot.bennies.BennyColor;
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

public class UseBennyCommand implements ICommand {

	@Override
	public String getName() {
		return "use";
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		return "use benny";
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix) {

		try {
			if(args.length <3) {
				event.getChannel().sendMessage("Непонятно чо хотел..");
				return;
			}

			IGuild guild = event.getGuild();
			IUser user = Users.findUser(args[1], guild);
			if (user == null ) {
				event.getChannel().sendMessage("Непонятно кто руки сует..");
				return;
			}

			Pocket pocket = Poсkets.getPocket(guild, user);
			BennyColor color = BennyColor.getColor(args[2]);
			if (color == null ) {
				event.getChannel().sendMessage("Непонятно чо за цвет..");
				return;
			}
			if (pocket.use(color)) {
				StringBuilder reply = new StringBuilder();
				reply.append(user.mention()).append(" использовал ").append(color).append(" фишку.\n");
				IChannel ch = event.getChannel();
				ch.sendMessage(reply.toString());
				return;
			}
			event.getChannel().sendMessage("Нету у тебя такой фишки..");
		} catch (MissingArgumentsException e) {
			Messages.showError("Ничего не понимаю..", event);
		}

	}

}
