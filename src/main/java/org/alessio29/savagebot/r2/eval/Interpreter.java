package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.internal.Messages;
import org.alessio29.savagebot.r2.Dumper;
import org.alessio29.savagebot.r2.tree.NonParsedStringStatement;
import org.alessio29.savagebot.r2.tree.Statement;

import java.util.List;

public class Interpreter {
    private final CommandContext context;
    private boolean debugEnabled = false;

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
            if (debugEnabled) {
                result.append("\n`").append(statement.getText()).append("`:\n")
                        .append(Messages.BLOCK_MARKER).append("\n")
                        .append(Dumper.dump(statement))
                        .append(Messages.BLOCK_MARKER).append("\n");
            }

            try {
                result.append(statement.accept(new StatementInterpreter(this)));
            } catch (EvaluationErrorException e) {
                result.append("{").append(statement.getText()).append(": ").append(e.getMessage()).append("}");
            }
        }

        return result.toString();
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
}
