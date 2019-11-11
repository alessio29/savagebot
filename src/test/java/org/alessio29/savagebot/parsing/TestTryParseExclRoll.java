package org.alessio29.savagebot.parsing;

import org.alessio29.savagebot.apiActions.diceRolls.ParseAndRollAction;
import org.alessio29.savagebot.r2.Dumper;
import org.alessio29.savagebot.r2.tree.Statement;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.List;

public class TestTryParseExclRoll {
    @Test
    public void testBlaBla() {
        expect(null, "blabla");
        expect(null, "bla bla");
    }

    @Test
    public void testSimpleRoll() {
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
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: Int 2\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null",
                "r2d6"
        );
    }

    @Test
    public void testSimpleRollWithModifier() {
        expect(
                "RollOnce\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: GenericRoll isOpenEnded=false\n" +
                        "      diceCount: null\n" +
                        "      facetsCount: Int 20\n" +
                        "      suffixArg1: null\n" +
                        "      suffixArg2: null\n" +
                        "    arg2: Int 4",
                "d20+4"
        );
        expect(
                "RollOnce\n" +
                        "  expr: Operator PLUS\n" +
                        "    arg1: GenericRoll isOpenEnded=false\n" +
                        "      diceCount: null\n" +
                        "      facetsCount: Int 20\n" +
                        "      suffixArg1: null\n" +
                        "      suffixArg2: null\n" +
                        "    arg2: Int 4",
                "rd20+4"
        );
    }

    @Test
    public void testRepeatedRoll() {
        expect(
                "RollTimes\n" +
                        "  times: Int 6\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=KEEP\n" +
                        "    diceCount: Int 4\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: Int 3\n" +
                        "    suffixArg2: null",
                "6x4d6k3"
        );
        expect(
                "RollTimes\n" +
                        "  times: Int 6\n" +
                        "  expr: GenericRoll isOpenEnded=false suffixOperator=KEEP\n" +
                        "    diceCount: Int 4\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: Int 3\n" +
                        "    suffixArg2: null",
                "r6x4d6k3"
        );
    }

    private void expect(String expectedDump, String arg) {
        List<Statement> statements = ParseAndRollAction.tryParseStatements(arg);
        if (statements == null) {
            Assert.assertNull(expectedDump);
            return;
        }
        StringWriter sw = new StringWriter();
        for (Statement statement : statements) {
            statement.accept(new Dumper(sw));
        }
        Assert.assertEquals(TestUtils.normalize(expectedDump), TestUtils.normalize(sw.toString()));
    }
}
