package org.alessio29.savagebot.r2.parse;

import org.alessio29.savagebot.r2.grammar.R2Lexer;
import org.alessio29.savagebot.r2.grammar.R2Parser;
import org.alessio29.savagebot.r2.tree.ErrorStatement;
import org.alessio29.savagebot.r2.tree.NonParsedStringStatement;
import org.alessio29.savagebot.r2.tree.Statement;
import org.antlr.v4.runtime.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
                statements.addAll(parseCommandElement(next.toString()));
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
            statements.addAll(parseCommandElement(next.toString()));
        }

        return statements;
    }

    private static class SyntaxErrorException extends RuntimeException {
        SyntaxErrorException(String message) {
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

    public List<Statement> parseCommandElement(String input) {
        String stmtString = input.trim();
        if (stmtString.length() == 0) {
            return Collections.emptyList();
        }

        R2Lexer lexer = new R2Lexer(CharStreams.fromString(stmtString));
        lexer.removeErrorListeners();
        lexer.addErrorListener(THROWING_ERROR_LISTENER);

        R2Parser parser = new R2Parser(new BufferedTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(THROWING_ERROR_LISTENER);

        try {
            return parser.commandElement().statement().stream()
                    .map(statementContext -> {
                        try {
                            return new StatementDesugarer(stmtString).visit(statementContext);
                        } catch (DesugaringErrorExceptioon e) {
                            return new ErrorStatement(stmtString, e.getMessage());
                        }
                    })
                    .collect(Collectors.toList());
        } catch (SyntaxErrorException e) {
            return Collections.singletonList(
                    new NonParsedStringStatement(stmtString, e.getMessage())
            );
        }
    }

    public static String desugarComment(String string) {
        return string.substring(1, string.length() - 1);
    }

}
