package org.alessio29.savagebot.apiActions.diceRolls;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.ExpressionEvaluator;
import org.alessio29.savagebot.r2.eval.IntListResult;
import org.alessio29.savagebot.r2.eval.IntResult;
import org.alessio29.savagebot.r2.parse.Parser;
import org.alessio29.savagebot.r2.tree.Expression;
import org.alessio29.savagebot.r2.tree.RollOnceStatement;
import org.alessio29.savagebot.r2.tree.Statement;

import java.util.List;
import java.util.StringJoiner;

public class RollEmpireOfPetalThroneHPAction implements IBotAction {
    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("Expected arguments: <level> <hit_die_expression>", args.length + 1);
        }

        try {
            CommandContext cc = new CommandContext();
            return new CommandExecutionResult(run(args, cc), args.length + 1);
        } catch (Throwable e) {
            return new CommandExecutionResult(e.getMessage(), args.length + 1);
        }
    }

    public String run(String[] args, CommandContext cc) {
        List<Statement> parsedArgs = new Parser().parse(args);
        if (parsedArgs.size() != 2) {
            throw new RuntimeException("Expected 2 arguments: <level> <hit_die_expression>");
        }

        Statement levelStmt = parsedArgs.get(0);
        if (!(levelStmt instanceof RollOnceStatement)) {
            throw new RuntimeException("<level> argument should be an expression");
        }
        Statement hitDiceStmt = parsedArgs.get(1);
        if (!(hitDiceStmt instanceof RollOnceStatement)) {
            throw new RuntimeException("<hit_die_expression> should be an expression");
        }

        Expression levelExpr = ((RollOnceStatement) levelStmt).getExpression();
        Expression hitDieExpr = ((RollOnceStatement) hitDiceStmt).getExpression();

        StringBuilder result = new StringBuilder();

        int currentHP = 0;
        int level = eval(levelExpr, cc).getValue();

        for (int i = 0; i < level; ++i) {
            int limit = currentHP + 1;

            int currentLevel = i + 1;
            result.append("At level ").append(currentLevel).append(": ");
            int total = 0;
            StringJoiner sumJoiner = new StringJoiner(" + ");
            for (int k = 0; k < currentLevel; ++k) {
                IntResult hitDie = eval(hitDieExpr, cc);
                int hitDieValue = Math.max(hitDie.getValue(), 1);
                sumJoiner.add(Integer.toString(hitDieValue));
                total += hitDieValue;
            }
            result.append(sumJoiner.toString()).append(" = ");
            if (total < limit) {
                result.append(total).append(", too low, ")
                        .append(ReplyBuilder.bold(limit))
                        .append(" taken");
                currentHP = limit;
            } else {
                result.append(ReplyBuilder.bold(total));
                currentHP = total;
            }
            result.append("\n");
        }

        result.append("HP: ").append(ReplyBuilder.bold(currentHP));

        return result.toString();
    }

    private IntResult eval(Expression expression, CommandContext cc) {
        IntListResult result = ExpressionEvaluator.evalUnsafe(expression, cc);
        if (result.getValues().size() != 1) {
            throw new RuntimeException("Single result expression expected: `" + expression.getText() + "`");
        }
        return new IntResult(result.getValues().get(0), result.getExplained());
    }
}
