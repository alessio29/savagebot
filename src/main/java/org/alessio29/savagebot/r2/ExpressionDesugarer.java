package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.r2.grammar.R2Parser;
import org.alessio29.savagebot.r2.tree.*;
import org.antlr.v4.runtime.tree.ParseTree;

class ExpressionDesugarer extends Desugarer<Expression> {
    ExpressionDesugarer(String inputString) {
        super(inputString);
    }

    @Override
    public Expression visitTermExpr(R2Parser.TermExprContext ctx) {
        return visit(ctx.t);
    }

    @Override
    public Expression visitIntTerm(R2Parser.IntTermContext ctx) {
        String text = ctx.getText();
        try {
            return new IntExpression(text, Integer.parseInt(text));
        } catch (NumberFormatException e) {
            throw new DesugaringErrorExceptioon("Unrecognized integer: '" + text + "'", e);
        }
    }

    @Override
    public Expression visitExprTerm(R2Parser.ExprTermContext ctx) {
        Expression innerExpression;

        if (ctx.comment != null) {
            innerExpression = new CommentedExpression(
                    getOriginalText(ctx),
                    Parser.desugarComment(ctx.comment.getText()),
                    visit(ctx.e)
            );
        } else {
            innerExpression = visit(ctx.e);
        }

        return new OperatorExpression(
                getOriginalText(ctx),
                OperatorExpression.Operator.BRACKETS,
                innerExpression
        );
    }

    @Override
    public Expression visitInfixExpr1(R2Parser.InfixExpr1Context ctx) {
        return new OperatorExpression(
                getOriginalText(ctx),
                OperatorExpression.getBinaryOperator(ctx.op.getText()),
                visit(ctx.e1),
                visit(ctx.e2)
        );
    }

    @Override
    public Expression visitInfixExpr2(R2Parser.InfixExpr2Context ctx) {
        return new OperatorExpression(
                getOriginalText(ctx),
                OperatorExpression.getBinaryOperator(ctx.op.getText()),
                visit(ctx.e1),
                visit(ctx.e2)
        );
    }

    @Override
    public Expression visitPrefixExpr(R2Parser.PrefixExprContext ctx) {
        return new OperatorExpression(
                getOriginalText(ctx),
                OperatorExpression.getUnaryOperator(ctx.op.getText()),
                visit(ctx.e1)
        );
    }

    @Override
    public Expression visitGenericRollExpr(R2Parser.GenericRollExprContext ctx) {
        R2Parser.GenericRollContext gr = ctx.genericRoll();

        Expression arg1 = visitOrNull(gr.t1);
        Expression arg2 = visit(gr.t2);
        boolean isOpenEnded = gr.excl != null;

        R2Parser.GenericRollSuffixContext grs = gr.genericRollSuffix();
        if (grs == null) {
            if (arg1 == null && arg2 instanceof IntExpression) {
                int facets = ((IntExpression) arg2).getValue();
                switch (facets) {
                    case 66:
                        return new D66RollExpression(getOriginalText(ctx), 2);
                    case 666:
                        return new D66RollExpression(getOriginalText(ctx), 3);
                    case 6666:
                        return new D66RollExpression(getOriginalText(ctx), 4);
                    case 66666:
                        return new D66RollExpression(getOriginalText(ctx), 5);
                    case 666666:
                        return new D66RollExpression(getOriginalText(ctx), 6);
                    case 6666666:
                        return new D66RollExpression(getOriginalText(ctx), 7);
                    case 66666666:
                        return new D66RollExpression(getOriginalText(ctx), 8);
                    case 666666666:
                        return new D66RollExpression(getOriginalText(ctx), 9);
                }
            }

            return new GenericRollExpression(getOriginalText(ctx), arg1, arg2, isOpenEnded);
        }

        return new GenericRollExpression(
                getOriginalText(ctx),
                arg1, arg2, isOpenEnded,
                GenericRollExpression.getSuffixOperator(grs.op.getText()),
                visitOrNull(grs.n)
        );
    }

    @Override
    public Expression visitFudgeRollExpr(R2Parser.FudgeRollExprContext ctx) {
        R2Parser.FudgeRollContext frc = ctx.fudgeRoll();

        return new FudgeRollExpression(
                getOriginalText(ctx),
                visitOrNull(frc.t)
        );
    }

    @Override
    public Expression visitSavageWorldsRollExpr(R2Parser.SavageWorldsRollExprContext ctx) {
        R2Parser.SavageWorldsRollContext swrc = ctx.savageWorldsRoll();

        return new SavageWorldsRollExpression(
                getOriginalText(ctx),
                visitOrNull(swrc.t1),
                visit(swrc.t2),
                visitOrNull(swrc.t3)
        );
    }

    private Expression visitOrNull(ParseTree parseTree) {
        return parseTree == null ? null : visit(parseTree);
    }
}
