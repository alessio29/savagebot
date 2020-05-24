package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.bennies.BennyType;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.utils.ChannelConfigs;

import java.util.HashMap;
import java.util.Map;

public class TakeBenniesAction implements IBotAction {



    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        BennyType bType = ChannelConfigs.getChannelConfig(message.getChannelId()).getBennyType();
        if (bType.equals(BennyType.DEADLANDS)) {
            if (args.length < 2) {
                return new CommandExecutionResult("Command syntax: tb <CharName> <bennyColor>", args.length + 1);
            }
            String charName = args[0];
            Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), charName);
            String bennyColor = args[1];
            if (character == null) {
                return new CommandExecutionResult(charName + " not found.", args.length + 1);
            }
            if (!character.hasBennies(bennyColor)) {
                return new CommandExecutionResult("Character " + charName + " has no such bennies.", args.length + 1);
            }
            character.useBenny(bennyColor);
            BennyColor bc = BennyColor.get(bennyColor);
            return new CommandExecutionResult("Character " + charName + " used "+bc.toString().toLowerCase()+" benny.", args.length + 1);
        } else {
            if (args.length < 1) {
                return new CommandExecutionResult("Command syntax: tb <CharName>", args.length + 1);
            }
            String charName = args[0];
            Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), charName);
            if (character == null) {
                return new CommandExecutionResult(charName + " not found.", args.length + 1);
            }
            if (character.getBennies() <= 0) {
                return new CommandExecutionResult("Character " + charName + " has no bennies.", args.length + 1);
            }
            character.useBenny();
            return new CommandExecutionResult("Character " + charName + " used benny.", args.length + 1);
        }
    }
}
