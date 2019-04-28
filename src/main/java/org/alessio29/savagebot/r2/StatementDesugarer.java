package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.r2.grammar.R2Parser;
import org.alessio29.savagebot.r2.tree.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.stream.Collectors;

class StatementDesugarer extends Desugarer<Statement> {
    StatementDesugarer(String inputString) {
        super(inputString);
    }

    @Override
    public Statement visitRollOnceStmt(R2Parser.RollOnceStmtContext ctx) {
        return new RollOnceStatement(
                getOriginalText(ctx),
                desugarExpression(ctx.e)
        );
    }

    @Override
    public Statement visitRollTimesStmt(R2Parser.RollTimesStmtContext ctx) {
        return new RollTimesStatement(
                getOriginalText(ctx),
                desugarExpression(ctx.n),
                desugarExpression(ctx.e)
        );
    }

    @Override
    public Statement visitRollBatchTimesStmt(R2Parser.RollBatchTimesStmtContext ctx) {
        return new RollBatchTimesStatement(
                getOriginalText(ctx),
                desugarExpression(ctx.n),
                ctx.batchElement().stream()
                        .map(be -> {
                            Expression expression = desugarExpression(be.e);
                            if (be.comment != null) {
                                return new CommentedExpression(be.getText(), Parser.desugarComment(be.comment.getText()), expression);
                            }
                            return expression;
                        })
                        .collect(Collectors.toList())
        );
    }

    private Expression desugarExpression(ParseTree parseTree) {
        return new ExpressionDesugarer(inputString).visit(parseTree);
    }
}
