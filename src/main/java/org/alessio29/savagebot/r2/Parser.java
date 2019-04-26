package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.r2.grammar.R2BaseVisitor;
import org.alessio29.savagebot.r2.grammar.R2Lexer;
import org.alessio29.savagebot.r2.grammar.R2Parser;
import org.alessio29.savagebot.r2.grammar.R2Visitor;
import org.alessio29.savagebot.r2.tree.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public List<Statement> parse(String[] args) {
        String input = String.join(" ", args);
        List<Statement> statements = new ArrayList<>();

        StringBuilder next = new StringBuilder();

        int bracketsBalance = 0;
        int squareBracketsBalance = 0;
        int curlyBracketsBalance = 0;
        boolean isInStringLiteral = false;

        for (int i = 0, length = input.length(); i < length; ++i) {
            char ch = input.charAt(i);

            if (ch == '\\' && i + 1 < length) {
                next.append(input.charAt(++i));
            } else if ((Character.isWhitespace(ch) || ch == ';') &&
                    bracketsBalance == 0 &&
                    squareBracketsBalance == 0 &&
                    curlyBracketsBalance == 0 &&
                    !isInStringLiteral
            ) {
                Statement stmt = parseSingleStatement(next.toString());
                if (stmt != null) {
                    statements.add(stmt);
                }
                next.setLength(0);
            } else {
                next.append(ch);
                switch (ch) {
                    case '(':
                        ++bracketsBalance;
                        break;
                    case ')':
                        if (bracketsBalance > 0) {
                            --bracketsBalance;
                        }
                        break;
                    case '[':
                        ++squareBracketsBalance;
                        break;
                    case ']':
                        if (squareBracketsBalance > 0) {
                            --squareBracketsBalance;
                        }
                        break;
                    case '{':
                        ++curlyBracketsBalance;
                        break;
                    case '}':
                        if (curlyBracketsBalance > 0) {
                            --curlyBracketsBalance;
                        }
                        break;
                    case '"':
                        isInStringLiteral = !isInStringLiteral;
                        break;
                }
            }
        }

        if (next.length() > 0) {
            Statement stmt = parseSingleStatement(next.toString());
            if (stmt != null) {
                statements.add(stmt);
            }
        }

        return statements;
    }

    private static class SyntaxErrorException extends RuntimeException {
        public SyntaxErrorException(String message) {
            super(message);
        }
    }

    private static class DesugaringErrorExceptioon extends RuntimeException {
        public DesugaringErrorExceptioon(String message) {
            super(message);
        }

        public DesugaringErrorExceptioon(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static final ANTLRErrorListener THROWING_ERROR_LISTENER = new BaseErrorListener() {
        @Override
        public void syntaxError(
                Recognizer<?, ?> recognizer,
                Object offendingSymbol,
                int line,
                int charPositionInLine,
                String msg,
                RecognitionException e
        ) {
            throw new SyntaxErrorException("[" + charPositionInLine + "]: " + msg);
        }
    };

    private Statement parseSingleStatement(String input) {
        String stmtString = input.trim();
        if (stmtString.length() == 0) {
            return null;
        }

        R2Lexer lexer = new R2Lexer(CharStreams.fromString(stmtString));
        lexer.removeErrorListeners();
        lexer.addErrorListener(THROWING_ERROR_LISTENER);

        R2Parser parser = new R2Parser(new BufferedTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(THROWING_ERROR_LISTENER);

        try {
            R2Parser.StatementContext stmtCtx = parser.statement();
            try {
                return STATEMENT_DESUGARER.visit(stmtCtx);
            } catch (DesugaringErrorExceptioon e) {
                return new ErrorStatement(stmtString, e.getMessage());
            }
        } catch (SyntaxErrorException e) {
            return new NonParsedStringStatement(stmtString, e.getMessage());
        }
    }

    private static final R2Visitor<Statement> STATEMENT_DESUGARER = new R2BaseVisitor<Statement>() {
        @Override
        public Statement visitRollOnceStmt(R2Parser.RollOnceStmtContext ctx) {
            return new RollOnceStatement(
                    ctx.getText(),
                    desugarExpression(ctx.e)
            );
        }

        @Override
        public Statement visitRollTimesStmt(R2Parser.RollTimesStmtContext ctx) {
            return new RollTimesStatement(
                    ctx.getText(),
                    desugarExpression(ctx.n),
                    desugarExpression(ctx.e)
            );
        }

        private Expression desugarExpression(ParseTree parseTree) {
            return EXPRESSION_DESUGARER.visit(parseTree);
        }
    };

    private static final R2Visitor<Expression> EXPRESSION_DESUGARER = new R2BaseVisitor<Expression>() {
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
            return visit(ctx.e);
        }

        @Override
        public Expression visitInfixExpr1(R2Parser.InfixExpr1Context ctx) {
            return new OperatorExpression(
                    ctx.getText(),
                    OperatorExpression.getBinaryOperator(ctx.op.getText()),
                    visit(ctx.e1),
                    visit(ctx.e2)
            );
        }

        @Override
        public Expression visitInfixExpr2(R2Parser.InfixExpr2Context ctx) {
            return new OperatorExpression(
                    ctx.getText(),
                    OperatorExpression.getBinaryOperator(ctx.op.getText()),
                    visit(ctx.e1),
                    visit(ctx.e2)
            );
        }

        @Override
        public Expression visitPrefixExpr(R2Parser.PrefixExprContext ctx) {
            // Currently all unary operators are prefix
            return new OperatorExpression(
                    ctx.getText(),
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
                return new GenericRollExpression(ctx.getText(), arg1, arg2, isOpenEnded);
            }

            return new GenericRollExpression(
                    ctx.getText(),
                    arg1, arg2, isOpenEnded,
                    GenericRollExpression.getSuffixOperator(grs.op.getText()),
                    visitOrNull(grs.n)
            );
        }

        @Override
        public Expression visitFudgeRollExpr(R2Parser.FudgeRollExprContext ctx) {
            R2Parser.FudgeRollContext frc = ctx.fudgeRoll();

            return new FudgeRollExpression(
                    ctx.getText(),
                    visitOrNull(frc.t)
            );
        }

        @Override
        public Expression visitSavageWorldsRollExpr(R2Parser.SavageWorldsRollExprContext ctx) {
            R2Parser.SavageWorldsRollContext swrc = ctx.savageWorldsRoll();

            return new SavageWorldsRollExpression(
                    ctx.getText(),
                    visitOrNull(swrc.t1),
                    visit(swrc.t2),
                    visitOrNull(swrc.t3)
            );
        }

        private Expression visitOrNull(ParseTree parseTree) {
            return parseTree == null ? null : visit(parseTree);
        }
    };
}
