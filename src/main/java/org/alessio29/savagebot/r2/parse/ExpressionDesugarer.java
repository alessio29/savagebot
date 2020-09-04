package org.alessio29.savagebot.r2.parse;

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
    public Expression visitAssignExpr(R2Parser.AssignExprContext ctx) {
        return new AssignVariableExpression(
                getOriginalText(ctx),
                ctx.v.getText(),
                visit(ctx.e1)
        );
    }

    @Override
    public Expression visitVarTerm(R2Parser.VarTermContext ctx) {
        return new VariableExpression(
                getOriginalText(ctx),
                ctx.v.getText()
        );
    }

    @Override
    public Expression visitBoundedExpr(R2Parser.BoundedExprContext ctx) {
        if (ctx.e2 == null && ctx.e3 == null) {
            throw new DesugaringErrorExceptioon("At least one bound should be provided: `" + getOriginalText(ctx) + "`");
        }

        Expression argument2 = visitOrNull(ctx.e2);
        Expression argument3 = visitOrNull(ctx.e3);
        if (argument2 instanceof IntExpression && argument3 instanceof IntExpression) {
            int value2 = ((IntExpression) argument2).getValue();
            int value3 = ((IntExpression) argument3).getValue();
            if (value2 > value3) {
                throw new DesugaringErrorExceptioon("Empty range: `" + getOriginalText(ctx) + "`");
            }
        }

        return new OperatorExpression(
                getOriginalText(ctx),
                OperatorExpression.Operator.BOUND_TO,
                visit(ctx.e1),
                argument2,
                argument3
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

    private Expression desugarDieFacets(R2Parser.DieFacetsTermContext ctx) {
        if (ctx.getText().equals("%")) {
            return new IntExpression(getOriginalText(ctx), 100);
        } else {
            return visit(ctx.term());
        }
    }

    @Override
    public Expression visitGenericRollExpr(R2Parser.GenericRollExprContext ctx) {
        R2Parser.GenericRollContext gr = ctx.genericRoll();

        Expression arg1 = visitOrNull(gr.t1);
        Expression arg2 = desugarDieFacets(gr.t2);
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
        } else if (grs instanceof R2Parser.RollAndKeepSuffixContext) {
            R2Parser.RollAndKeepSuffixContext suffix = (R2Parser.RollAndKeepSuffixContext) grs;
            String suffixOperatorText = suffix.op.getText();
            GenericRollExpression.SuffixOperator suffixOperator = GenericRollExpression.getSuffixOperator(suffixOperatorText);
            if (suffixOperator.getRequiredArguments() > 0 && suffix.n == null) {
                throw new DesugaringErrorExceptioon("Argument required for '" + suffixOperatorText + "'");
            }
            return new GenericRollExpression(
                    getOriginalText(ctx),
                    arg1, arg2, isOpenEnded,
                    suffixOperator,
                    visitOrNull(suffix.n)
            );
        } else if (grs instanceof R2Parser.SuccessOrFailSuffix1Context) {
            R2Parser.SuccessOrFailSuffix1Context suffix = (R2Parser.SuccessOrFailSuffix1Context) grs;
            return desugarSuccessOrFailure(getOriginalText(ctx), arg1, arg2, isOpenEnded, suffix.sn, suffix.fn);
        } else if (grs instanceof R2Parser.SuccessOrFailSuffix2Context) {
            R2Parser.SuccessOrFailSuffix2Context suffix = (R2Parser.SuccessOrFailSuffix2Context) grs;
            return desugarSuccessOrFailure(getOriginalText(ctx), arg1, arg2, isOpenEnded, suffix.sn, suffix.fn);
        } else if (grs instanceof R2Parser.TargetNumberAndRaiseStepSuffixContext) {
            R2Parser.TargetNumberAndRaiseStepContext tnrc =
                    ((R2Parser.TargetNumberAndRaiseStepSuffixContext) grs).targetNumberAndRaiseStep();
            return new GenericRollExpression(
                    getOriginalText(ctx),
                    arg1, arg2, isOpenEnded,
                    visitOrNull(tnrc.tt), visitOrNull(tnrc.tr), visitOrNull(tnrc.tnr)
            );
        }
        else {
            throw new DesugaringErrorExceptioon("Unexpected generic roll suffix: '" + grs.getText() + "': " +
                    grs.getClass().getSimpleName());
        }
    }

    private Expression desugarSuccessOrFailure(
            String originalText,
            Expression arg1,
            Expression arg2,
            boolean isOpenEnded,
            R2Parser.TermContext sn,
            R2Parser.TermContext fn
    ) {
        return new GenericRollExpression(
                originalText, arg1, arg2, isOpenEnded,
                GenericRollExpression.SuffixOperator.SUCCESS_OR_FAIL,
                visitOrNull(sn),
                visitOrNull(fn)
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
    public Expression visitCarcosaRollExpr(R2Parser.CarcosaRollExprContext ctx) {
        return new CarcosaRollExpression(
                getOriginalText(ctx),
                visitOrNull(ctx.carcosaRoll().t)
        );
    }

    @Override
    public Expression visitWegD6RollExpr(R2Parser.WegD6RollExprContext ctx) {
        return new WegD6RollExpression(
                getOriginalText(ctx),
                visitOrNull(ctx.wegD6Roll().t)
        );
    }

    @Override
    public Expression visitSavageWorldsRollExpr(R2Parser.SavageWorldsRollExprContext ctx) {
        R2Parser.SavageWorldsRollContext swrc = ctx.savageWorldsRoll();

        TargetNumberAndRaiseStep tnr =
                desugarTargetNumberAndRaiseStep(
                        swrc.targetNumberAndRaiseStep(),
                        TargetNumberMode.SAVAGE_WORLDS_SUCCESSES_AND_RAISES
                );

        return new SavageWorldsRollExpression(
                getOriginalText(ctx),
                visitOrNull(swrc.t1),
                visit(swrc.t2),
                visitOrNull(swrc.t3),
                tnr.getTargetNumber(),
                tnr.getRaiseStep(),
                tnr.getTargetNumberAndRaiseStep()
        );
    }

    @Override
    public Expression visitSavageWorldsExtrasRollExpr(R2Parser.SavageWorldsExtrasRollExprContext ctx) {
        R2Parser.SavageWorldsExtrasRollContext swerc = ctx.savageWorldsExtrasRoll();
        TargetNumberAndRaiseStep tnr =
                desugarTargetNumberAndRaiseStep(
                        swerc.targetNumberAndRaiseStep(),
                        TargetNumberMode.SAVAGE_WORLDS_SUCCESSES_AND_RAISES
                );
        return new SavageWorldsExtrasRollExpression(
                getOriginalText(ctx),
                visitOrNull(swerc.t1),
                null,
                null,
                tnr.getTargetNumber(),
                tnr.getRaiseStep(),
                tnr.getTargetNumberAndRaiseStep()
        );
    }

    @Override
    public Expression visitTargetNumberAndRaiseStepExpr(R2Parser.TargetNumberAndRaiseStepExprContext ctx) {
        Expression arg = visit(ctx.e1);

        TargetNumberAndRaiseStep tnr =
                desugarTargetNumberAndRaiseStep(
                        ctx.targetNumberAndRaiseStep(),
                        arg instanceof SavageWorldsRollExpression || arg instanceof SavageWorldsExtrasRollExpression
                                ? TargetNumberMode.SAVAGE_WORLDS_SUCCESSES_AND_RAISES
                                : TargetNumberMode.SAVAGE_WORLDS_DAMAGE
                );

        return new TargetNumberAndRaiseStepExpression(
                getOriginalText(ctx),
                tnr.getMode(),
                tnr.getTargetNumber(),
                tnr.getRaiseStep(),
                tnr.getTargetNumberAndRaiseStep(),
                arg
        );
    }

    public Expression visitOrNull(ParseTree parseTree) {
        return parseTree == null ? null : visit(parseTree);
    }

    public TargetNumberAndRaiseStep desugarTargetNumberAndRaiseStep(
            R2Parser.TargetNumberAndRaiseStepContext tnrc,
            TargetNumberMode defaultMode
    ) {
        Expression tExpr;
        Expression rExpr;
        Expression trExpr;
        TargetNumberMode mode = defaultMode;
        if (tnrc == null) {
            tExpr = rExpr = trExpr = null;
        } else if (tnrc.tn != null) {
            tExpr = visitOrNull(tnrc.tn);
            rExpr = trExpr = null;
            if (tnrc.getText().endsWith("-")) {
                mode = TargetNumberMode.GENERIC_TN_ROLL_UNDER;
            } else {
                mode = TargetNumberMode.GENERIC_TN_ROLL_ABOVE;
            }
        } else if (tnrc.tnr != null) {
            trExpr = visitOrNull(tnrc.tnr);
            tExpr = rExpr = null;
        } else {
            tExpr = visitOrNull(tnrc.tt);
            rExpr = visitOrNull(tnrc.tr);
            trExpr = null;
        }

        return new TargetNumberAndRaiseStep(mode, tExpr, rExpr, trExpr);
    }
}
