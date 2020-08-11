package org.alessio29.savagebot.apiActions.diceRolls;

import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.RollInterpreter;
import org.alessio29.savagebot.r2.parse.Parser;
import org.alessio29.savagebot.r2.tree.NonParsedStringStatement;
import org.alessio29.savagebot.r2.tree.Statement;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ParseAndRollAction {
    @Nullable
    public static List<Statement> tryParseStatements(String command) {
        Parser parser = new Parser();
        List<Statement> statements = parser.parseCommandElement(command);
        if (hasParsedStatements(statements)) {
            return statements;
        }
        if ((command.startsWith("r") || command.startsWith("R")) && command.length() > 1) {
            statements = parser.parseCommandElement(command.substring(1));
            if (hasParsedStatements(statements)) {
                return statements;
            }
        }
        return null;
    }

    private static boolean hasParsedStatements(List<Statement> statements) {
        return !statements.stream().allMatch(statement -> statement instanceof NonParsedStringStatement);
    }

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        String command = args[0];
        List<Statement> statements = tryParseStatements(command);
        if (statements == null) return null;

        String result = new RollInterpreter(new CommandContext()).run(statements);

        return new CommandExecutionResult(result, 1);
    }
}
