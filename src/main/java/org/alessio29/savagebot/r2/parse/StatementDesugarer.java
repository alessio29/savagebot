package org.alessio29.savagebot.r2.parse;

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

    @Override
    public Statement visitFlagStmt(R2Parser.FlagStmtContext ctx) {
        return new FlagStatement(
                getOriginalText(ctx),
                ctx.flag.getText().substring(2)
        );
    }

    @Override
    public Statement visitRollSavageWorldsExtraStmt(R2Parser.RollSavageWorldsExtraStmtContext ctx) {
        ExpressionDesugarer expressionDesugarer = new ExpressionDesugarer(inputString);

        Expression n = expressionDesugarer.visit(ctx.n);
        Expression facets = expressionDesugarer.visit(ctx.t1);

        TargetNumberAndRaiseStep tnrs =
                expressionDesugarer.desugarTargetNumberAndRaiseStep(
                        ctx.targetNumberAndRaiseStep(),
                        TargetNumberMode.SAVAGE_WORLDS_SUCCESSES_AND_RAISES
                );

        R2Parser.AdditiveModifierContext admc = ctx.additiveModifier();

        int start = ctx.n != null ? ctx.n.getStop().getStopIndex() + 1 : ctx.getStart().getStartIndex();
        int stop = ctx.getStop().getStopIndex();
        String text = inputString.substring(start, stop + 1);

        Expression rollExpr = new SavageWorldsExtrasRollExpression(
                text,
                facets,
                admc != null ? OperatorExpression.getBinaryOperator(admc.op.getText()) : null,
                admc != null ? expressionDesugarer.visit(admc.em) : null,
                tnrs.getTargetNumber(),
                tnrs.getRaiseStep(),
                tnrs.getTargetNumberAndRaiseStep()
        );

        return new RollTimesStatement(getOriginalText(ctx), n, rollExpr);
    }

    private Expression desugarExpression(ParseTree parseTree) {
        return new ExpressionDesugarer(inputString).visit(parseTree);
    }
}
