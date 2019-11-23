package org.alessio29.savagebot.apiActions.diceRolls;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.RollInterpreter;
import org.alessio29.savagebot.r2.parse.Parser;

public class RollDiceAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("No commands", args.length + 1);
        }
        String result = new RollInterpreter(new CommandContext()).run(new Parser().parse(args));
        return new CommandExecutionResult(result, args.length + 1);
    }
}
