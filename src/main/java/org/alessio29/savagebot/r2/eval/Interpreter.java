package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.internal.builders.ReplyBuilder;
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

        ReplyBuilder result = new ReplyBuilder();

        for (Statement statement : statements) {
            if (debugEnabled) {
                result.newLine().
                        attach(statement.getText()).
                        newLine().
                        blockQoute().
                        newLine().
                        attach(Dumper.dump(statement)).
                        blockQoute().newLine();
            }

            try {
                result.attach(statement.accept(new StatementInterpreter(this)));
            } catch (EvaluationErrorException e) {
                result.attach("{").attach(statement.getText()).attach(": ").attach(e.getMessage()).attach("}");
            }
        }
        return result.toString();
    }

    void setDebugEnabled() {
        this.debugEnabled = true;
    }
}
