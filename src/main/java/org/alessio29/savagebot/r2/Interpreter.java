package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.r2.tree.NonParsedStringStatement;
import org.alessio29.savagebot.r2.tree.Statement;

import java.util.List;

public class Interpreter {
    private final CommandContext context;

    public Interpreter(CommandContext context) {
        this.context = context;
    }

    CommandContext getContext() {
        return context;
    }

    public String run(List<Statement> statements) {
        if (statements.stream().allMatch(s -> s instanceof NonParsedStringStatement)) {
            return "No commands";
        }

        StringBuilder result = new StringBuilder();

        for (Statement statement : statements) {
            try {
                result.append(statement.accept(new StatementInterpreter(this)));
            } catch (EvaluationErrorException e) {
                result.append("{").append(statement.getText()).append(": ").append(e.getMessage()).append("}");
            }
        }

        return result.toString();
    }

}
