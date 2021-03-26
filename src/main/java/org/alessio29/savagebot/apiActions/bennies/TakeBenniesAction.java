package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.bennies.BennyType;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.*;
import org.alessio29.savagebot.internal.utils.ChannelConfig;
import org.alessio29.savagebot.internal.utils.ChannelConfigs;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TakeBenniesAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }

        List<String> taken = new ArrayList<>();
        ParamsIterator it;
        ChannelConfig channelConfig = ChannelConfigs.getChannelConfig(message.getChannelId());
        if (channelConfig.normalBennies()) {
            it = new TakeBenniesParamsIterator(args);
        } else {
            // Deadlands Reloaded bennies
            it = new TakeColoredBenniesParamsIterator(args);
        }

        while (it.hasNext()) {
            String value = it.next().trim();
            if (!it.isEntity(value)) {
                return new CommandExecutionResult("Provide character name!", args.length+1);
            }

            Character character = Characters.getByNameOrCreate(message.getGuildId(), message.getChannelId(), value);
            if (!channelConfig.normalBennies()) {
                if (it.nextIsModifier()) {
                    taken.add((String)it.process(it.next().trim().toLowerCase(), character));
                    Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
                } else {
                    return new CommandExecutionResult("Provide benny type to give!", args.length + 1);
                }
            } else {
                String val = null;
                if (it.nextIsModifier()) {
                    val = it.next().trim().toLowerCase();
                }
                taken.add((String)it.process(val, character));
                Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
            }

        }
        return new CommandExecutionResult("Taken bennies from character(s): "+ StringUtils.join(taken, ", "), args.length + 1);



/* ==============================================
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
            return new CommandExecutionResult("Character " + charName + " used " + bc.toString().toLowerCase() + " benny.", args.length + 1);
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
        */
    }
}
