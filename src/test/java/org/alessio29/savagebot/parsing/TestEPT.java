package org.alessio29.savagebot.parsing;

import org.alessio29.savagebot.TestUtils;
import org.alessio29.savagebot.apiActions.diceRolls.RollEmpireOfPetalThroneHPAction;
import org.alessio29.savagebot.r2.eval.CommandContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class TestEPT {
    @Test
    public void testEmpireOfPetalThroneHP() {
        expect(
                "At level 1: 1 = **1**\n" +
                        "At level 2: 2 + 1 = **3**\n" +
                        "At level 3: 1 + 1 + 1 = 3, too low, **4** taken\n" +
                        "At level 4: 1 + 1 + 1 + 2 = **5**\n" +
                        "At level 5: 1 + 1 + 1 + 1 + 2 = **6**\n" +
                        "HP: **6**",
                new String[] { "5", "d4-2" }
        );
        expect(
                "At level 1: 1 = **1**\n" +
                        "At level 2: 5 + 2 = **7**\n" +
                        "At level 3: 6 + 6 + 6 = **18**\n" +
                        "At level 4: 6 + 4 + 4 + 3 = 17, too low, **19** taken\n" +
                        "At level 5: 6 + 6 + 6 + 5 + 6 = **29**\n" +
                        "HP: **29**",
                new String[] { "5", "d6" }
        );
    }

    private void expect(String expectedResult, String[] args) {
        CommandContext context = new CommandContext(new Random(0));
        String actual = TestUtils.normalize(new RollEmpireOfPetalThroneHPAction().run(args, context));
        String expected = TestUtils.normalize(expectedResult);
        Assert.assertEquals(expected, actual);
    }
}
