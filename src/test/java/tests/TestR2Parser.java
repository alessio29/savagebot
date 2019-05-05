package tests;

import org.alessio29.savagebot.r2.Dumper;
import org.alessio29.savagebot.r2.Parser;
import org.alessio29.savagebot.r2.tree.Statement;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.List;

public class TestR2Parser {
    @Test
    public void testEmpty() {
        expect("");
        expect("", "");
    }

    @Test
    public void testRandomStrings() {
        expect(
                "NonParsedString text='abc' parserErrorMessage='[0]: token recognition error at: 'ab''",
                "abc"
        );
        expect(
                "NonParsedString text='abc' parserErrorMessage='[0]: token recognition error at: 'ab''\n" +
                        "NonParsedString text='def' parserErrorMessage='[1]: token recognition error at: 'e''\n" +
                        "NonParsedString text='qwer' parserErrorMessage='[0]: token recognition error at: 'q''\n" +
                        "NonParsedString text='tty' parserErrorMessage='[0]: token recognition error at: 't''\n" +
                        "RollOnce\n" +
                        "  expr: Int 123",
                "abc def", "  qwer tty", "123"
        );
    }

    @Test
    public void testArithmetic() {
        expect(
                "RollOnce\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: Int 1\n" +
                        "    arg2: Int 1",
                "1+1"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: Int 2\n" +
                        "    arg2: Operator MUL\n" +
                        "      arg1: Int 2\n" +
                        "      arg2: Int 2",
                "2+2*2"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator MUL\n" +
                        "    arg1: Operator BRACKETS\n" +
                        "      arg1: Operator PLUS\n" +
                        "        arg1: Int 2\n" +
                        "        arg2: Int 2\n" +
                        "    arg2: Int 2",
                "(2+2)*2"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Int 2\n" +
                        "RollOnce\n" +
                        "  expr: Operator UNARY_MINUS\n" +
                        "    arg1: Int 2\n" +
                        "RollOnce\n" +
                        "  expr: Operator UNARY_PLUS\n" +
                        "    arg1: Int 2",
                "2", "-2", "+2"
        );
    }

    @Test
    public void testGenericDice() {
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "d6"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: Int 2\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "2d6"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: GenericRoll isOpenEnded=false\n" +
                        "      diceCount: Int 2\n" +
                        "      facetsCount: Int 6\n" +
                        "      suffixArg1: null\n" +
                        "      suffixArg2: null\n" +
                        "    arg2: Int 5",
                "2d6+5"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=KEEP\n" +
                        "    diceCount: Int 4\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: Int 3\n" +
                        "    suffixArg2: null",
                "4d6k3"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=KEEP_LEAST\n" +
                        "    diceCount: Int 4\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: Int 3\n" +
                        "    suffixArg2: null",
                "4d6kl3"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=ADVANTAGE\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 20\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "d20adv"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=ADVANTAGE\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 20\n" +
                        "    suffixArg1: Int 2\n" +
                        "    suffixArg2: null",
                "d20adv2"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=DISADVANTAGE\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 20\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "d20dis"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=DISADVANTAGE\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 20\n" +
                        "    suffixArg1: Int 2\n" +
                        "    suffixArg2: null",
                "d20dis2"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: GenericRoll isOpenEnded=false suffixOperator=ADVANTAGE\n" +
                        "      diceCount: null\n" +
                        "      facetsCount: Int 20\n" +
                        "      suffixArg1: null\n" +
                        "      suffixArg2: null\n" +
                        "    arg2: Int 3",
                "d20adv+3"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: GenericRoll isOpenEnded=true suffixOperator=KEEP\n" +
                        "      diceCount: Int 2\n" +
                        "      facetsCount: Int 10\n" +
                        "      suffixArg1: Int 1\n" +
                        "      suffixArg2: null\n" +
                        "    arg2: Int 6",
                "2d10!k1+6"
        );
    }

    @Test
    public void testSavageWorldsDice() {
        expect(
                "RollOnce\n" +
                        "  expr: SavageWorldsRoll\n" +
                        "    diceCount: null\n" +
                        "    abilityDie: Int 8\n" +
                        "    wildDie: null",
                "s8"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: SavageWorldsRoll\n" +
                        "      diceCount: null\n" +
                        "      abilityDie: Int 8\n" +
                        "      wildDie: null\n" +
                        "    arg2: Int 2",
                "s8+2"
        );
        expect(
                "RollOnce\n" +
                        "  expr: SavageWorldsRoll\n" +
                        "    diceCount: Int 2\n" +
                        "    abilityDie: Int 8\n" +
                        "    wildDie: null",
                "2s8"
        );
        expect(
                "RollOnce\n" +
                        "  expr: SavageWorldsRoll\n" +
                        "    diceCount: Int 2\n" +
                        "    abilityDie: Int 8\n" +
                        "    wildDie: Int 10",
                "2s8w10"
        );
    }

    @Test
    public void testFudgeDice() {
        expect(
                "RollOnce\n" +
                        "  expr: FudgeRoll\n" +
                        "    diceCount: null",
                "df"
        );
        expect(
                "RollOnce\n" +
                        "  expr: FudgeRoll\n" +
                        "    diceCount: Int 4",
                "4df"
        );
        expect(
                "RollOnce\n" +
                        "  expr: FudgeRoll\n" +
                        "    diceCount: Int 4",
                "4DF"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: Operator PLUS\n" +
                        "      arg1: SavageWorldsRoll\n" +
                        "        diceCount: null\n" +
                        "        abilityDie: Int 8\n" +
                        "        wildDie: null\n" +
                        "      arg2: FudgeRoll\n" +
                        "        diceCount: Int 2\n" +
                        "    arg2: Int 2",
                "s8+2df+2"
        );
    }

    @Test
    public void testCommented() {
        expect(
                "RollOnce\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: Operator PLUS\n" +
                        "      arg1: GenericRoll isOpenEnded=false\n" +
                        "        diceCount: null\n" +
                        "        facetsCount: Int 20\n" +
                        "        suffixArg1: null\n" +
                        "        suffixArg2: null\n" +
                        "      arg2: Operator BRACKETS\n" +
                        "        arg1: Commented \"STR\"\n" +
                        "          expr: Int 3\n" +
                        "    arg2: Operator BRACKETS\n" +
                        "      arg1: Commented \"Attack bonus\"\n" +
                        "        expr: Int 5",
                "d20+(\"STR\" 3)+(\"Attack bonus\" 5)"
        );
    }

    @Test
    public void testRollBatch() {
        expect(
                "RollBatch\n" +
                        "  times: Int 10\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 100\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: GenericRoll isOpenEnded=false\n" +
                        "      diceCount: Int 2\n" +
                        "      facetsCount: Int 6\n" +
                        "      suffixArg1: null\n" +
                        "      suffixArg2: null\n" +
                        "    arg2: Int 1\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: Int 2\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "10x[d100 2d6+1 2d6]"
        );
        expect(
                "RollBatch\n" +
                        "  times: Int 2\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 4\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "2x[d6 d4]"
        );
    }

    @Test
    public void testD66() {
        expect(
                "RollOnce\n" +
                        "  expr: RollD66 digits=2",
                "d66"
        );
        expect(
                "RollOnce\n" +
                        "  expr: RollD66 digits=3",
                "d666"
        );
        expect(
                "RollOnce\n" +
                        "  expr: RollD66 digits=4",
                "d6666"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: Int 1\n" +
                        "    facetsCount: Int 66\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "1d66"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Operator BRACKETS\n" +
                        "      arg1: Int 66\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "d(66)"
        );
    }

    @Test
    public void testCommandSeparation() {
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null\n" +
                        "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "d6 d6"
        );
        expect(
                "NonParsedString text='d6d6' parserErrorMessage='[2]: mismatched input 'd' expecting {<EOF>, ';'}'",
                "d6d6"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null\n" +
                        "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "d6", "d6"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null\n" +
                        "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: null\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "d6;d6"
        );
    }

    @Test
    public void testSuffixOperatorArguments() {
        expect(
                "Error text='2d6k' errorMessage='Argument required for 'k''",
                "2d6k"
        );
        expect(
                "Error text='2d6kl' errorMessage='Argument required for 'kl''",
                "2d6kl"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=ADVANTAGE\n" +
                        "    diceCount: Int 2\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "2d6adv"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=DISADVANTAGE\n" +
                        "    diceCount: Int 2\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "2d6dis"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=SUCCESS_OR_FAIL\n" +
                        "    diceCount: Int 9\n" +
                        "    facetsCount: Int 10\n" +
                        "    suffixArg1: Int 7\n" +
                        "    suffixArg2: Int 1",
                "9d10f1s7"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=SUCCESS_OR_FAIL\n" +
                        "    diceCount: Int 9\n" +
                        "    facetsCount: Int 10\n" +
                        "    suffixArg1: Int 7\n" +
                        "    suffixArg2: Int 1",
                "9d10s7f1"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=SUCCESS_OR_FAIL\n" +
                        "    diceCount: Int 9\n" +
                        "    facetsCount: Int 10\n" +
                        "    suffixArg1: Int 7\n" +
                        "    suffixArg2: null",
                "9d10s7"
        );
        expect(
                "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=true suffixOperator=SUCCESS_OR_FAIL\n" +
                        "    diceCount: Int 28\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: Int 8\n" +
                        "    suffixArg2: null",
                "28d6!s8"
        );
    }

    @Test
    public void testBoundedExpr() {
        expect(
                "RollOnce\n" +
                        "  expr: Operator BOUND_TO\n" +
                        "    arg1: GenericRoll isOpenEnded=false\n" +
                        "      diceCount: Int 10\n" +
                        "      facetsCount: Int 6\n" +
                        "      suffixArg1: null\n" +
                        "      suffixArg2: null\n" +
                        "    arg2: Int 15\n" +
                        "    arg3: Int 50",
                "10d6[15:50]"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator BOUND_TO\n" +
                        "    arg1: GenericRoll isOpenEnded=false\n" +
                        "      diceCount: Int 10\n" +
                        "      facetsCount: Int 6\n" +
                        "      suffixArg1: null\n" +
                        "      suffixArg2: null\n" +
                        "    arg2: Int 15\n" +
                        "    arg3: null",
                "10d6[15:]"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator BOUND_TO\n" +
                        "    arg1: GenericRoll isOpenEnded=false\n" +
                        "      diceCount: Int 10\n" +
                        "      facetsCount: Int 6\n" +
                        "      suffixArg1: null\n" +
                        "      suffixArg2: null\n" +
                        "    arg2: null\n" +
                        "    arg3: Int 50",
                "10d6[:50]"
        );
        expect(
                "Error text='10d6[:]' errorMessage='At least one bound should be provided: `10d6[:]`'",
                "10d6[:]"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: GenericRoll isOpenEnded=false\n" +
                        "      diceCount: Int 2\n" +
                        "      facetsCount: Int 6\n" +
                        "      suffixArg1: null\n" +
                        "      suffixArg2: null\n" +
                        "    arg2: Operator BOUND_TO\n" +
                        "      arg1: GenericRoll isOpenEnded=false\n" +
                        "        diceCount: Int 2\n" +
                        "        facetsCount: Int 6\n" +
                        "        suffixArg1: null\n" +
                        "        suffixArg2: null\n" +
                        "      arg2: Int 3\n" +
                        "      arg3: Int 8",
                "2d6+2d6[3:8]"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator MUL\n" +
                        "    arg1: GenericRoll isOpenEnded=false\n" +
                        "      diceCount: Int 2\n" +
                        "      facetsCount: Int 6\n" +
                        "      suffixArg1: null\n" +
                        "      suffixArg2: null\n" +
                        "    arg2: Operator BOUND_TO\n" +
                        "      arg1: GenericRoll isOpenEnded=false\n" +
                        "        diceCount: Int 2\n" +
                        "        facetsCount: Int 6\n" +
                        "        suffixArg1: null\n" +
                        "        suffixArg2: null\n" +
                        "      arg2: Int 3\n" +
                        "      arg3: Int 8",
                "2d6*2d6[3:8]"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator UNARY_PLUS\n" +
                        "    arg1: Operator BOUND_TO\n" +
                        "      arg1: GenericRoll isOpenEnded=false\n" +
                        "        diceCount: Int 2\n" +
                        "        facetsCount: Int 6\n" +
                        "        suffixArg1: null\n" +
                        "        suffixArg2: null\n" +
                        "      arg2: Int 3\n" +
                        "      arg3: Int 8",
                "+2d6[3:8]"
        );
    }

    private void expect(String expectedDump, String... args) {
        List<Statement> statements = new Parser().parse(args);
        StringWriter sw = new StringWriter();
        for (Statement statement : statements) {
            statement.accept(new Dumper(sw));
        }
        Assert.assertEquals(expectedDump.replaceAll("\n", System.lineSeparator()), sw.toString().trim());
    }
}
