package org.alessio29.savagebot.commands.dice;

import org.alessio29.savagebot.internal.Roll;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;
import com.Cardinal.CommandPackage.Exceptions.MissingRequirementsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class ShortRollDiceCommand implements ICommand {

		@Override
		public String getName() {

			return "r";
		}

		@Override
		public Category getCategory() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String[] getArguments() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void execute(MessageReceivedEvent event, String[] args, String prefix)
				throws MissingRequirementsException, MissingArgumentsException {
			
			Roll.execute(event, args, prefix);
		}
}
