package org.alessio29.savagebot.apiActions.states;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.ClearStatesParamIterator;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClearStatesAction implements IBotAction {

    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("Please provide character name or 'all' to clear all characters", 1);
        }
        ClearStatesParamIterator it;
        if (args[0].trim().equalsIgnoreCase("all")) {
            Map<String, Character> chars = Characters.getCharacters(message.getGuildId(), message.getChannelId());
            String [] st = new String[chars.keySet().size()];
            chars.keySet().toArray(st);
            it = new ClearStatesParamIterator(st);
        } else {
            it = new ClearStatesParamIterator(args);
        }
        List<String> list = new ArrayList<>();
        while (it.hasNext()) {
            String value = it.next();
            if (!it.isEntity(value)) {
                return new CommandExecutionResult("Provide character name!", args.length + 1);
            }
            Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), value);
            if (character == null) {
               continue;
            }
            list.add(character.getName());
            character.clearStates();
            Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
        }
        String response = "States cleared from character(s) " + StringUtils.join(list, ", ");
        return new CommandExecutionResult(response, args.length + 1);
    }
}
