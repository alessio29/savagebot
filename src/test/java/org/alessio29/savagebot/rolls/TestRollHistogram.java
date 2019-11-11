package org.alessio29.savagebot.rolls;

import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.RollAccumulatingInterpreter;
import org.alessio29.savagebot.r2.parse.Parser;
import org.alessio29.savagebot.r2.tree.Statement;
import org.junit.Assert;
import org.junit.Test;
import tests.TestUtils;

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
                ":\n" +
                        "**6**: 1;",
                "2d6"
        );
    }

    @Test
    public void testHistogramSeveralRolls() {
        expect(
                ":\n" +
                        "**6**: 1;\n" +
                        "**8**: 1;\n" +
                        "**12**: 1;",
                "2d6", "2d6", "2d6"
        );
    }

    @Test
    public void testHistogramRepeatedRoll() {
        expect(
                ":\n" +
                        "**2**: 15;\n" +
                        "**3**: 32;\n" +
                        "**4**: 36;\n" +
                        "**5**: 61;\n" +
                        "**6**: 62;\n" +
                        "**7**: 75;\n" +
                        "**8**: 71;\n" +
                        "**9**: 61;\n" +
                        "**10**: 38;\n" +
                        "**11**: 33;\n" +
                        "**12**: 16;",
                "500x2d6"
        );
    }

    @Test
    public void testMultipleHistograms() {
        expect(
                ":\n" +
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

    private void expect(String expectedResult, String... args) {
        List<Statement> statements = new Parser().parse(args);
        CommandContext context = new CommandContext(new Random(0));
        RollAccumulatingInterpreter interpreter = new RollAccumulatingInterpreter(context);
        String actualResult = TestUtils.normalize(interpreter.rollHistogram(statements));
        String expected = TestUtils.normalize(expectedResult);
        Assert.assertEquals(expected, actualResult);
    }
}
