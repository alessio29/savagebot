package tests;

import org.alessio29.savagebot.r2.CommandContext;
import org.alessio29.savagebot.r2.Interpreter;
import org.alessio29.savagebot.r2.Parser;
import org.alessio29.savagebot.r2.tree.Statement;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class TestR2Interpreter {
    @Test
    public void testRandomText() {
        expect(
                "No commands",
                "ha ha ha"
        );
        expect(
                "bla bla bla 1234 = **1234**",
                "bla", "bla", "bla", "1234"
        );
    }

    @Test
    public void testArithmetic() {
        expect(
                "2 + 2 = **4**",
                "2+2"
        );
        expect(
                "2 + 2 * 2 = **6**",
                "2+2*2"
        );
        expect(
                "1/0: Division by 0",
                "1/0"
        );
        expect(
                "1%0: Division by 0",
                "1%0"
        );
    }

    @Test
    public void testGenericRolls() {
        expect(
                "{2d6: [1,5]} = **6**",
                "2d6"
        );
        expect(
                "{10d6!: [1,5,2,6+6+6+6+4=28,4,3,6+6+6+5=23,6+3=9,5,4]} = **84**",
                "10d6!"
        );
        expect(
                "{4d6k3: [~~1~~,2,5,6]} = **13**",
                "4d6k3"
        );
        expect(
                "{3d6: [1,5,2]} + {2d4: [3,3]} = **14**",
                "3d6+2d4"
        );
        expect(
                "{d20a: [~~1~~,9]} = **9**",
                "d20a"
        );
        expect(
                "{d20a1: [~~1~~,9]} = **9**",
                "d20a1"
        );
        expect(
                "{d20d: [1,~~9~~]} = **1**",
                "d20d"
        );
        expect(
                "{d20d1: [1,~~9~~]} = **1**",
                "d20d1"
        );
    }

    @Test
    public void testFudgeRolls() {
        expect(
                "{df: [-..+]} = **0**",
                "df"
        );
        expect(
                "{10DF: [-..++++--+]} = **2**",
                "10DF"
        );
        expect(
                "{3d6: [1,5,2]} + {df: [++++]} = **12**",
                "3d6+df"
        );
    }

    @Test
    public void testSavageWorldsRolls() {
        expect(
                "{s8: [wild: 5; 6]} = **6**",
                "s8"
        );
        expect(
                "{s8: [wild: 5; 6]} + 2 = **8**",
                "s8+2"
        );
        expect(
                "{s8: [wild: 5; 6]} + {2df: [.+]} = **7**",
                "s8+2df"
        );
        expect(
                "{3s8: [wild: 6+6+6+6+4=28; 2; 6; 7]} = **6**, **7**, **28**",
                "3s8"
        );
        expect(
                "{3s8w10: [wild: 8; 2; 6; 7]} = **6**, **7**, **8**",
                "3s8w10"
        );
        expect(
                "{3s6: [wild: 6+6+6+6+4=28; 1; 2; 5]} + 2 = **4**, **7**, **30**",
                "3s6+2"
        );
    }

    @Test
    public void testRepeatedRolls() {
        expect(
                "10x2d6: \n" +
                        "1: {2d6: [1,5]} = **6**\n" +
                        "2: {2d6: [2,6]} = **8**\n" +
                        "3: {2d6: [6,6]} = **12**\n" +
                        "4: {2d6: [6,4]} = **10**\n" +
                        "5: {2d6: [4,3]} = **7**\n" +
                        "6: {2d6: [6,6]} = **12**\n" +
                        "7: {2d6: [6,5]} = **11**\n" +
                        "8: {2d6: [6,3]} = **9**\n" +
                        "9: {2d6: [5,4]} = **9**\n" +
                        "10: {2d6: [6,3]} = **9**",
                "10x2d6"
        );
        expect(
                "10x3s6+2: \n" +
                        "1: {3s6: [wild: 6+6+6+6+4=28; 1; 2; 5]} + 2 = **4**, **7**, **30**\n" +
                        "2: {3s6: [wild: 6+3=9; 3; 4; 6+6+6+5=23]} + 2 = **6**, **11**, **25**\n" +
                        "3: {3s6: [wild: 2; 4; 5; 6+3=9]} + 2 = **6**, **7**, **11**\n" +
                        "4: {3s6: [wild: 5; 1; 3; 6+3=9]} + 2 = **5**, **7**, **11**\n" +
                        "5: {3s6: [wild: 6+6+6+6+6+3=33; 1; 4; 5]} + 2 = **6**, **7**, **35**\n" +
                        "6: {3s6: [wild: 4; 3; 4; 6+5=11]} + 2 = **6**, **6**, **13**\n" +
                        "7: {3s6: [wild: 5; 2; 3; 6+3=9]} + 2 = **5**, **7**, **11**\n" +
                        "8: {3s6: [wild: 5; 4; 5; 5]} + 2 = **7**, **7**, **7**\n" +
                        "9: {3s6: [wild: 3; 1; 2; 4]} + 2 = **4**, **5**, **6**\n" +
                        "10: {3s6: [wild: 5; 1; 4; 5]} + 2 = **6**, **7**, **7**",
                "10x3s6+2"
        );
    }

    @Test
    public void testRollsWithRollsInRolls() {
        expect(
                "{(d6)d6: [5]} = **5**",
                "(d6)d6"
        );
        expect(
                "{(d4+2)d6: [5,2,6,6,6]} = **25**",
                "(d4+2)d6"
        );
        expect(
                "(2d6)x2d6: ({2d6: [1,5]}) = **6**\n" +
                        "1: {2d6: [2,6]} = **8**\n" +
                        "2: {2d6: [6,6]} = **12**\n" +
                        "3: {2d6: [6,4]} = **10**\n" +
                        "4: {2d6: [4,3]} = **7**\n" +
                        "5: {2d6: [6,6]} = **12**\n" +
                        "6: {2d6: [6,5]} = **11**",
                "(2d6)x2d6"
        );
    }

    @Test
    public void testCommented() {
        expect(
                "{d20: [1]} + (STR: 3) + (Attack bonus: 5) = **9**",
                "d20+(\"STR\" 3)+(\"Attack bonus\" 5)"
        );
    }

    @Test
    public void testRollBatchRepeated() {
        expect(
                "10x[d100 2d6+1 2d6]: \n" +
                        "1: {d100: [61]} = **61**; {2d6: [5,2]} + 1 = **8**; {2d6: [6,6]} = **12**; \n" +
                        "2: {d100: [54]} = **54**; {2d6: [6,4]} + 1 = **11**; {2d6: [4,3]} = **7**; \n" +
                        "3: {d100: [78]} = **78**; {2d6: [6,6]} + 1 = **13**; {2d6: [5,6]} = **11**; \n" +
                        "4: {d100: [45]} = **45**; {2d6: [5,4]} + 1 = **10**; {2d6: [6,3]} = **9**; \n" +
                        "5: {d100: [44]} = **44**; {2d6: [1,3]} + 1 = **5**; {2d6: [6,3]} = **9**; \n" +
                        "6: {d100: [61]} = **61**; {2d6: [4,5]} + 1 = **10**; {2d6: [1,6]} = **7**; \n" +
                        "7: {d100: [46]} = **46**; {2d6: [6,6]} + 1 = **13**; {2d6: [6,3]} = **9**; \n" +
                        "8: {d100: [63]} = **63**; {2d6: [4,6]} + 1 = **11**; {2d6: [5,4]} = **9**; \n" +
                        "9: {d100: [61]} = **61**; {2d6: [2,6]} + 1 = **9**; {2d6: [3,5]} = **8**; \n" +
                        "10: {d100: [92]} = **92**; {2d6: [5,5]} + 1 = **11**; {2d6: [5,1]} = **6**;",
                "10x[d100 2d6+1 2d6]"
        );
        expect(
                "(2d6)x[d100 2d6+1 2d6]: ({2d6: [1,5]}) = **6**\n" +
                        "1: {d100: [30]} = **30**; {2d6: [6,6]} + 1 = **13**; {2d6: [6,6]} = **12**; \n" +
                        "2: {d100: [62]} = **62**; {2d6: [4,3]} + 1 = **8**; {2d6: [6,6]} = **12**; \n" +
                        "3: {d100: [74]} = **74**; {2d6: [5,6]} + 1 = **12**; {2d6: [3,5]} = **8**; \n" +
                        "4: {d100: [76]} = **76**; {2d6: [6,3]} + 1 = **10**; {2d6: [2,1]} = **3**; \n" +
                        "5: {d100: [25]} = **25**; {2d6: [6,3]} + 1 = **10**; {2d6: [5,4]} = **9**; \n" +
                        "6: {d100: [83]} = **83**; {2d6: [1,6]} + 1 = **8**; {2d6: [6,6]} = **12**;",
                "(2d6)x[d100 2d6+1 2d6]"
        );
        expect(
                "10x[\"rating\" d100 \"weight\" 2d6+1 2d6]: \n" +
                        "1: rating: {d100: [61]} = **61**; weight: {2d6: [5,2]} + 1 = **8**; {2d6: [6,6]} = **12**; \n" +
                        "2: rating: {d100: [54]} = **54**; weight: {2d6: [6,4]} + 1 = **11**; {2d6: [4,3]} = **7**; \n" +
                        "3: rating: {d100: [78]} = **78**; weight: {2d6: [6,6]} + 1 = **13**; {2d6: [5,6]} = **11**; \n" +
                        "4: rating: {d100: [45]} = **45**; weight: {2d6: [5,4]} + 1 = **10**; {2d6: [6,3]} = **9**; \n" +
                        "5: rating: {d100: [44]} = **44**; weight: {2d6: [1,3]} + 1 = **5**; {2d6: [6,3]} = **9**; \n" +
                        "6: rating: {d100: [61]} = **61**; weight: {2d6: [4,5]} + 1 = **10**; {2d6: [1,6]} = **7**; \n" +
                        "7: rating: {d100: [46]} = **46**; weight: {2d6: [6,6]} + 1 = **13**; {2d6: [6,3]} = **9**; \n" +
                        "8: rating: {d100: [63]} = **63**; weight: {2d6: [4,6]} + 1 = **11**; {2d6: [5,4]} = **9**; \n" +
                        "9: rating: {d100: [61]} = **61**; weight: {2d6: [2,6]} + 1 = **9**; {2d6: [3,5]} = **8**; \n" +
                        "10: rating: {d100: [92]} = **92**; weight: {2d6: [5,5]} + 1 = **11**; {2d6: [5,1]} = **6**;",
                "10x[\"rating\" d100 \"weight\" 2d6+1 2d6]"
        );
    }

    @Test
    public void testD66() {
        expect(
                "{d66: [1,5]} = **15**",
                "d66"
        );
        expect(
                "{d666: [1,5,2]} = **152**",
                "d666"
        );
        expect(
                "{d6666: [1,5,2,6]} = **1526**",
                "d6666"
        );
    }

    private void expect(String result, String... args) {
        List<Statement> statements = new Parser().parse(args);
        CommandContext context = new CommandContext(new Random(0));
        Interpreter interpreter = new Interpreter(context);
        String actualResult = interpreter.run(statements).trim();
        if (actualResult.contains("\n") && !actualResult.contains(System.lineSeparator())) {
            actualResult = actualResult.replace("\n", System.lineSeparator());
        }
        String expected = result.replace("\n", System.lineSeparator()).trim();
        Assert.assertEquals(expected, actualResult);
    }
}
