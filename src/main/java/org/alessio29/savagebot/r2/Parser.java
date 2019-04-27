package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.r2.grammar.R2Lexer;
import org.alessio29.savagebot.r2.grammar.R2Parser;
import org.alessio29.savagebot.r2.tree.*;
import org.antlr.v4.runtime.*;

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
                        if (!isInStringLiteral) {
                            ++bracketsBalance;
                        }
                        break;
                    case ')':
                        if (!isInStringLiteral && bracketsBalance > 0) {
                            --bracketsBalance;
                        }
                        break;
                    case '[':
                        if (!isInStringLiteral) {
                            ++squareBracketsBalance;
                        }
                        break;
                    case ']':
                        if (!isInStringLiteral && squareBracketsBalance > 0) {
                            --squareBracketsBalance;
                        }
                        break;
                    case '{':
                        if (!isInStringLiteral) {
                            ++curlyBracketsBalance;
                        }
                        break;
                    case '}':
                        if (!isInStringLiteral && curlyBracketsBalance > 0) {
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
                return new StatementDesugarer(stmtString).visit(stmtCtx);
            } catch (DesugaringErrorExceptioon e) {
                return new ErrorStatement(stmtString, e.getMessage());
            }
        } catch (SyntaxErrorException e) {
            return new NonParsedStringStatement(stmtString, e.getMessage());
        }
    }

    public static String desugarComment(String string) {
        return string.substring(1, string.length() - 1);
    }

}
