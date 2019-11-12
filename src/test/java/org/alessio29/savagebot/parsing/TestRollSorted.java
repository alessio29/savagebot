package org.alessio29.savagebot.parsing;

import org.alessio29.savagebot.TestUtils;
import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.RollAccumulatingInterpreter;
import org.alessio29.savagebot.r2.parse.Parser;
import org.alessio29.savagebot.r2.tree.Statement;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class TestRollSorted {
    @Test
    public void testSortedSimple() {
        expect(
                "Charlie: d20: **10**\n" +
                        "Betty: d20: **9**\n" +
                        "Dorothee: d20: **8**\n" +
                        "Alex: d20: **1**",
                "Alex", "d20",
                "Betty", "d20",
                "Charlie", "d20",
                "Dorothee", "d20"
        );
        expect(
                "Betty: d20+3: 9 + 3 = **12**\n" +
                        "Charlie: d20+1: 10 + 1 = **11**\n" +
                        "Dorothee: d20: **8**\n" +
                        "Alex: d20+5: 1 + 5 = **6**",
                "Alex", "d20+5",
                "Betty", "d20+3",
                "Charlie", "d20+1",
                "Dorothee", "d20"
        );
    }

    @Test
    public void testBlaBlaBla() {
        expect(
                "bla bla bla",
                "bla bla bla"
        );
        expect(
                "bla bla bla",
                "bla", "bla", "bla"
        );
    }

    @Test
    public void testSortedWithTimes() {
        expect(
                "Bad guys 3 of 4: d20: **16**\n" +
                        "Bad guys 4 of 4: d20: **14**\n" +
                        "Bad guys 1 of 4: d20: **10**\n" +
                        "Player B: d20+1: 9 + 1 = **10**\n" +
                        "Bad guys 2 of 4: d20: **8**\n" +
                        "Player A: d20+2: 1 + 2 = **3**",
                "Player", "A", "d20+2",
                "Player", "B", "d20+1",
                "Bad guys", "4xd20"
        );
    }

    @Test
    public void testSortedWithNonTrivialTimes() {
        expect(
                "(d6): (2) = **2**\n" +
                        "Bad guys 2 of 2: d20: **16**\n" +
                        "Player B: d20+1: 9 + 1 = **10**\n" +
                        "Bad guys 1 of 2: d20: **8**\n" +
                        "Player A: d20+2: 1 + 2 = **3**",
                "Player", "A", "d20+2",
                "Player", "B", "d20+1",
                "Bad guys", "(d6)xd20"
        );
    }

    @Test
    public void testSortedWithBatchTimes() {
        expect(
                "Bad guy teams 2 of 3: d20+2: 16 + 2 = **18**\n" +
                        "Bad guy teams 2 of 3: d20: **14**\n" +
                        "Bad guy teams 3 of 3: d20+2: 12 + 2 = **14**\n" +
                        "Bad guy teams 1 of 3: d20+2: 10 + 2 = **12**\n" +
                        "Player B: d20+1: 9 + 1 = **10**\n" +
                        "Bad guy teams 1 of 3: d20: **8**\n" +
                        "Player A: d20+2: 1 + 2 = **3**\n" +
                        "Bad guy teams 3 of 3: d20: **2**",
                "Player", "A", "d20+2",
                "Player", "B", "d20+1",
                "Bad guy teams", "3x[d20+2; d20]"
        );
        expect(
                "Bad guy teams 2 of 3 fast guy: d20+2: 16 + 2 = **18**\n" +
                        "Bad guy teams 2 of 3 slow guy: d20: **14**\n" +
                        "Bad guy teams 3 of 3 fast guy: d20+2: 12 + 2 = **14**\n" +
                        "Bad guy teams 1 of 3 fast guy: d20+2: 10 + 2 = **12**\n" +
                        "Player B: d20+1: 9 + 1 = **10**\n" +
                        "Bad guy teams 1 of 3 slow guy: d20: **8**\n" +
                        "Player A: d20+2: 1 + 2 = **3**\n" +
                        "Bad guy teams 3 of 3 slow guy: d20: **2**",
                "Player", "A", "d20+2",
                "Player", "B", "d20+1",
                "Bad guy teams", "3x[\"fast guy\" d20+2; \"slow guy\" d20]"
        );
    }

    private void expect(String expectedResult, String... args) {
        List<Statement> statements = new Parser().parse(args);
        CommandContext context = new CommandContext(new Random(0));
        RollAccumulatingInterpreter interpreter = new RollAccumulatingInterpreter(context);
        String actualResult = TestUtils.normalize(interpreter.rollSorted(statements));
        String expected = TestUtils.normalize(expectedResult);
        Assert.assertEquals(expected, actualResult);
    }
}
