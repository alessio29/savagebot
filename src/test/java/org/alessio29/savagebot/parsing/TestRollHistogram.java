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

public class TestRollHistogram {
    @Test
    public void testBlaBla() {
        expect(
                "bla bla bla",
                "bla", "bla", "bla"
        );
    }

    @Test
    public void testHistogramSingleRoll() {
        expect(
                "2d6:\n" +
                        "**6**: 2d6: 1;",
                "2d6"
        );
    }

    @Test
    public void testHistogramSeveralRolls() {
        expect(
                "2d6, 2d6, 2d6:\n" +
                        "**6**: 2d6: 1; 2d6: 0; 2d6: 0;\n" +
                        "**8**: 2d6: 0; 2d6: 1; 2d6: 0;\n" +
                        "**12**: 2d6: 0; 2d6: 0; 2d6: 1;",
                "2d6", "2d6", "2d6"
        );
    }

    @Test
    public void testHistogramRepeatedRoll() {
        expect(
                "2d6:\n" +
                        "**2**: 2d6: 15;\n" +
                        "**3**: 2d6: 32;\n" +
                        "**4**: 2d6: 36;\n" +
                        "**5**: 2d6: 61;\n" +
                        "**6**: 2d6: 62;\n" +
                        "**7**: 2d6: 75;\n" +
                        "**8**: 2d6: 71;\n" +
                        "**9**: 2d6: 61;\n" +
                        "**10**: 2d6: 38;\n" +
                        "**11**: 2d6: 33;\n" +
                        "**12**: 2d6: 16;",
                "500x2d6"
        );
    }

    @Test
    public void testMultipleHistograms() {
        expect(
                "a, b:\n" +
                        "**1**: a: 50; b: 0;\n" +
                        "**2**: a: 50; b: 11;\n" +
                        "**3**: a: 55; b: 24;\n" +
                        "**4**: a: 51; b: 32;\n" +
                        "**5**: a: 46; b: 42;\n" +
                        "**6**: a: 50; b: 58;\n" +
                        "**7**: a: 62; b: 59;\n" +
                        "**8**: a: 50; b: 68;\n" +
                        "**9**: a: 40; b: 90;\n" +
                        "**10**: a: 46; b: 75;\n" +
                        "**11**: a: 42; b: 97;\n" +
                        "**12**: a: 53; b: 82;\n" +
                        "**13**: a: 48; b: 77;\n" +
                        "**14**: a: 51; b: 69;\n" +
                        "**15**: a: 38; b: 65;\n" +
                        "**16**: a: 51; b: 47;\n" +
                        "**17**: a: 53; b: 49;\n" +
                        "**18**: a: 61; b: 23;\n" +
                        "**19**: a: 60; b: 23;\n" +
                        "**20**: a: 43; b: 9;",
                "a", "1000xd20", "b", "1000x2d10"
        );
    }

    @Test
    public void testMultipleHistogramsInBatch() {
        expect(
                "2d8, 2d6!:\n" +
                        "**2**: 2d8: 18; 2d6!: 26;\n" +
                        "**3**: 2d8: 36; 2d6!: 56;\n" +
                        "**4**: 2d8: 64; 2d6!: 81;\n" +
                        "**5**: 2d8: 67; 2d6!: 105;\n" +
                        "**6**: 2d8: 75; 2d6!: 163;\n" +
                        "**7**: 2d8: 98; 2d6!: 95;\n" +
                        "**8**: 2d8: 94; 2d6!: 85;\n" +
                        "**9**: 2d8: 128; 2d6!: 88;\n" +
                        "**10**: 2d8: 104; 2d6!: 47;\n" +
                        "**11**: 2d8: 88; 2d6!: 30;\n" +
                        "**12**: 2d8: 75; 2d6!: 45;\n" +
                        "**13**: 2d8: 64; 2d6!: 39;\n" +
                        "**14**: 2d8: 37; 2d6!: 32;\n" +
                        "**15**: 2d8: 39; 2d6!: 22;\n" +
                        "**16**: 2d8: 13; 2d6!: 18;\n" +
                        "**17**: 2d8: 0; 2d6!: 11;\n" +
                        "**18**: 2d8: 0; 2d6!: 18;\n" +
                        "**19**: 2d8: 0; 2d6!: 8;\n" +
                        "**20**: 2d8: 0; 2d6!: 10;\n" +
                        "**21**: 2d8: 0; 2d6!: 2;\n" +
                        "**22**: 2d8: 0; 2d6!: 3;\n" +
                        "**24**: 2d8: 0; 2d6!: 3;\n" +
                        "**25**: 2d8: 0; 2d6!: 3;\n" +
                        "**26**: 2d8: 0; 2d6!: 1;\n" +
                        "**27**: 2d8: 0; 2d6!: 3;\n" +
                        "**29**: 2d8: 0; 2d6!: 1;\n" +
                        "**30**: 2d8: 0; 2d6!: 2;\n" +
                        "**32**: 2d8: 0; 2d6!: 1;\n" +
                        "**33**: 2d8: 0; 2d6!: 1;\n" +
                        "**34**: 2d8: 0; 2d6!: 1;",
                "1000x[2d8; 2d6!]"
        );
    }

    private void expect(String expectedResult, String... args) {
        List<Statement> statements = new Parser().parse(args);
        CommandContext context = new CommandContext(new Random(0));
        RollAccumulatingInterpreter interpreter = new RollAccumulatingInterpreter(context);
        String actualResult = TestUtils.normalize(interpreter.rollHistogram(statements));
        String expected = TestUtils.normalize(expectedResult);
        Assert.assertEquals(expected, actualResult);
    }
}
