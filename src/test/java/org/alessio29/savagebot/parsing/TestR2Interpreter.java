package org.alessio29.savagebot.parsing;

import org.alessio29.savagebot.TestUtils;
import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.RollInterpreter;
import org.alessio29.savagebot.r2.parse.Parser;
import org.alessio29.savagebot.r2.tree.Statement;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class TestR2Interpreter {
    @Test
    public void testRandomText1() {
        expect(
                "No commands",
                "ha ha ha"
        );
        expect(
                "bla bla bla 1234",
                "bla", "bla", "bla", "1234"
        );
    }

    @Test
    public void testStrangeDiceArguments() {
        expect("d0: Facets count should be >0: 0", "d0");
        expect("d(1-2): Facets count should be >0: -1", "d(1-2)");
        expect("0", "0d1");
        expect("0d0:  = **0**", "0d0");
        expect("2d6k0: ~~1~~ + ~~5~~ = **0**", "2d6k0");
    }

    @Test
    public void testArithmetic() {
        expect(
                "2+2: 2 + 2 = **4**",
                "2+2"
        );
        expect(
                "2+2*2: 2 + 2 * 2 = **6**",
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
    public void testD1() {
        expect("10", "10d1");
    }

    @Test
    public void testGenericRolls() {
        expect(
                "2d6: 1 + 5 = **6**",
                "2d6"
        );
        expect(
                "10d6!: 1 + 5 + 2 + 28 + 4 + 3 + 23 + 9 + 5 + 4 = **84**",
                "10d6!"
        );
        expect(
                "4d6k3: ~~1~~ + 2 + 5 + 6 = **13**",
                "4d6k3"
        );
        expect(
                "3d6+2d4: 1 + 5 + 2 + 3 + 3 = **14**",
                "3d6+2d4"
        );
        expect(
                "d20adv: ~~1~~ + 9 = **9**",
                "d20adv"
        );
        expect(
                "d20adv1: ~~1~~ + 9 = **9**",
                "d20adv1"
        );
        expect(
                "d20dis: 1 + ~~9~~ = **1**",
                "d20dis"
        );
        expect(
                "d20dis1: 1 + ~~9~~ = **1**",
                "d20dis1"
        );
    }

    @Test
    public void testFudgeRolls() {
        expect(
                "df: [-00+] = **0** Mediocre",
                "df"
        );
        expect(
                "10DF: [-00++++--+] = **2**",
                "10DF"
        );
        expect(
                "3d6+df: 1 + 5 + 2 + [++++] = **12** Legendary+4",
                "3d6+df"
        );
        expect(
                "10xdf: \n" +
                        "1: df: [-00+] = **0** Mediocre\n" +
                        "2: df: [+++-] = **2** Fair\n" +
                        "3: df: [-+++] = **2** Fair\n" +
                        "4: df: [+0++] = **3** Good\n" +
                        "5: df: [0-++] = **1** Average\n" +
                        "6: df: [0-++] = **1** Average\n" +
                        "7: df: [+0-0] = **0** Mediocre\n" +
                        "8: df: [-+++] = **2** Fair\n" +
                        "9: df: [++++] = **4** Great\n" +
                        "10: df: [-+0-] = **-1** Poor",
                "10xdf"
        );
    }

    @Test
    public void testSavageWorldsRolls() {
        expect(
                "s8: [6; w5] = **6** (success)",
                "s8"
        );
        expect(
                "s8+2: [6; w5] + 2 = **8** (success; **1** raise)",
                "s8+2"
        );
        expect(
                "s8+2df: [6; w5] + [0+] = **7** (success)",
                "s8+2df"
        );
        expect(
                "3s8: [2; 6; 7; w28] = **6** (success), **7** (success), **28** (success; **6** raises)",
                "3s8"
        );
        expect(
                "3s8w10: [2; 6; 7; w8] = **6** (success), **7** (success), **8** (success; **1** raise)",
                "3s8w10"
        );
        expect(
                "3s6+2: [1; 2; 5; w28] + 2 = **4** (success), **7** (success), **30** (success; **6** raises)",
                "3s6+2"
        );
    }

    @Test
    public void testForbiddenOpenEndedRolls() {
        expect("1", "d1!");
        expect("s1: [1; w5] = **5** (success)", "s1");
        expect("e1: 1 = **1**", "e1");
    }

    @Test
    public void testRepeatedRolls() {
        expect(
                "6x3d6: \n" +
                        "1: 3d6: 1 + 5 + 2 = **8**\n" +
                        "2: 3d6: 6 + 6 + 6 = **18**\n" +
                        "3: 3d6: 6 + 4 + 4 = **14**\n" +
                        "4: 3d6: 3 + 6 + 6 = **15**\n" +
                        "5: 3d6: 6 + 5 + 6 = **17**\n" +
                        "6: 3d6: 3 + 5 + 4 = **12**",
                "6x3d6"
        );
        expect(
                "6x4d6k3: \n" +
                        "1: 4d6k3: ~~1~~ + 2 + 5 + 6 = **13**\n" +
                        "2: 4d6k3: ~~4~~ + 6 + 6 + 6 = **18**\n" +
                        "3: 4d6k3: ~~3~~ + 4 + 6 + 6 = **16**\n" +
                        "4: 4d6k3: ~~3~~ + 5 + 6 + 6 = **17**\n" +
                        "5: 4d6k3: ~~3~~ + 4 + 5 + 6 = **15**\n" +
                        "6: 4d6k3: ~~1~~ + 2 + 3 + 6 = **11**",
                "6x4d6k3"
        );
        expect(
                "10x2d6: \n" +
                        "1: 2d6: 1 + 5 = **6**\n" +
                        "2: 2d6: 2 + 6 = **8**\n" +
                        "3: 2d6: 6 + 6 = **12**\n" +
                        "4: 2d6: 6 + 4 = **10**\n" +
                        "5: 2d6: 4 + 3 = **7**\n" +
                        "6: 2d6: 6 + 6 = **12**\n" +
                        "7: 2d6: 6 + 5 = **11**\n" +
                        "8: 2d6: 6 + 3 = **9**\n" +
                        "9: 2d6: 5 + 4 = **9**\n" +
                        "10: 2d6: 6 + 3 = **9**",
                "10x2d6"
        );
        expect(
                "10x3s6+2: \n" +
                        "1: 3s6+2: [1; 2; 5; w28] + 2 = **4** (success), **7** (success), **30** (success; **6** raises)\n" +
                        "2: 3s6+2: [3; 4; 23; w9] + 2 = **6** (success), **11** (success; **1** raise), **25** (success; **5** raises)\n" +
                        "3: 3s6+2: [4; 5; 9; w2] + 2 = **6** (success), **7** (success), **11** (success; **1** raise)\n" +
                        "4: 3s6+2: [1; 3; 9; w5] + 2 = **5** (success), **7** (success), **11** (success; **1** raise)\n" +
                        "5: 3s6+2: [1; 4; 5; w33] + 2 = **6** (success), **7** (success), **35** (success; **7** raises)\n" +
                        "6: 3s6+2: [3; 4; 11; w4] + 2 = **6** (success), **6** (success), **13** (success; **2** raises)\n" +
                        "7: 3s6+2: [2; 3; 9; w5] + 2 = **5** (success), **7** (success), **11** (success; **1** raise)\n" +
                        "8: 3s6+2: [4; 5; 5; w5] + 2 = **7** (success), **7** (success), **7** (success)\n" +
                        "9: 3s6+2: [1; 2; 4; w3] + 2 = **4** (success), **5** (success), **6** (success)\n" +
                        "10: 3s6+2: [1; 4; 5; w5] + 2 = **6** (success), **7** (success), **7** (success)",
                "10x3s6+2"
        );
    }

    @Test
    public void testRollsWithRollsInRolls() {
        expect(
                "(d6)d6: 5 = **5**",
                "(d6)d6"
        );
        expect(
                "(d4+2)d6: 5 + 2 + 6 + 6 + 6 = **25**",
                "(d4+2)d6"
        );
        expect(
                "(2d6)x2d6: (2d6): (1 + 5) = **6**\n" +
                        "1: 2d6: 2 + 6 = **8**\n" +
                        "2: 2d6: 6 + 6 = **12**\n" +
                        "3: 2d6: 6 + 4 = **10**\n" +
                        "4: 2d6: 4 + 3 = **7**\n" +
                        "5: 2d6: 6 + 6 = **12**\n" +
                        "6: 2d6: 6 + 5 = **11**",
                "(2d6)x2d6"
        );
    }

    @Test
    public void testCommented() {
        expect(
                "d20+(\"STR\" 3)+(\"Attack bonus\" 5): 1 + (STR: 3) + (Attack bonus: 5) = **9**",
                "d20+(\"STR\" 3)+(\"Attack bonus\" 5)"
        );
    }

    @Test
    public void testRollBatchRepeated() {
        expect(
                "10x[d100 2d6+1 2d6]: \n" +
                        "1: d100: **61**; 2d6+1: 5 + 2 + 1 = **8**; 2d6: 6 + 6 = **12**; \n" +
                        "2: d100: **54**; 2d6+1: 6 + 4 + 1 = **11**; 2d6: 4 + 3 = **7**; \n" +
                        "3: d100: **78**; 2d6+1: 6 + 6 + 1 = **13**; 2d6: 5 + 6 = **11**; \n" +
                        "4: d100: **45**; 2d6+1: 5 + 4 + 1 = **10**; 2d6: 6 + 3 = **9**; \n" +
                        "5: d100: **44**; 2d6+1: 1 + 3 + 1 = **5**; 2d6: 6 + 3 = **9**; \n" +
                        "6: d100: **61**; 2d6+1: 4 + 5 + 1 = **10**; 2d6: 1 + 6 = **7**; \n" +
                        "7: d100: **46**; 2d6+1: 6 + 6 + 1 = **13**; 2d6: 6 + 3 = **9**; \n" +
                        "8: d100: **63**; 2d6+1: 4 + 6 + 1 = **11**; 2d6: 5 + 4 = **9**; \n" +
                        "9: d100: **61**; 2d6+1: 2 + 6 + 1 = **9**; 2d6: 3 + 5 = **8**; \n" +
                        "10: d100: **92**; 2d6+1: 5 + 5 + 1 = **11**; 2d6: 5 + 1 = **6**;",
                "10x[d100 2d6+1 2d6]"
        );
        expect(
                "(2d6)x[d100 2d6+1 2d6]: (2d6): (1 + 5) = **6**\n" +
                        "1: d100: **30**; 2d6+1: 6 + 6 + 1 = **13**; 2d6: 6 + 6 = **12**; \n" +
                        "2: d100: **62**; 2d6+1: 4 + 3 + 1 = **8**; 2d6: 6 + 6 = **12**; \n" +
                        "3: d100: **74**; 2d6+1: 5 + 6 + 1 = **12**; 2d6: 3 + 5 = **8**; \n" +
                        "4: d100: **76**; 2d6+1: 6 + 3 + 1 = **10**; 2d6: 2 + 1 = **3**; \n" +
                        "5: d100: **25**; 2d6+1: 6 + 3 + 1 = **10**; 2d6: 5 + 4 = **9**; \n" +
                        "6: d100: **83**; 2d6+1: 1 + 6 + 1 = **8**; 2d6: 6 + 6 = **12**;",
                "(2d6)x[d100 2d6+1 2d6]"
        );
    }

    @Test
    public void testRollBatchRepeatedWithInlineComments() {
        expect(
                "10x[\"rating\" d100 \"weight\" 2d6+1 2d6]: \n" +
                        "1: rating: 61 = **61**; weight: 5 + 2 + 1 = **8**; 2d6: 6 + 6 = **12**; \n" +
                        "2: rating: 54 = **54**; weight: 6 + 4 + 1 = **11**; 2d6: 4 + 3 = **7**; \n" +
                        "3: rating: 78 = **78**; weight: 6 + 6 + 1 = **13**; 2d6: 5 + 6 = **11**; \n" +
                        "4: rating: 45 = **45**; weight: 5 + 4 + 1 = **10**; 2d6: 6 + 3 = **9**; \n" +
                        "5: rating: 44 = **44**; weight: 1 + 3 + 1 = **5**; 2d6: 6 + 3 = **9**; \n" +
                        "6: rating: 61 = **61**; weight: 4 + 5 + 1 = **10**; 2d6: 1 + 6 = **7**; \n" +
                        "7: rating: 46 = **46**; weight: 6 + 6 + 1 = **13**; 2d6: 6 + 3 = **9**; \n" +
                        "8: rating: 63 = **63**; weight: 4 + 6 + 1 = **11**; 2d6: 5 + 4 = **9**; \n" +
                        "9: rating: 61 = **61**; weight: 2 + 6 + 1 = **9**; 2d6: 3 + 5 = **8**; \n" +
                        "10: rating: 92 = **92**; weight: 5 + 5 + 1 = **11**; 2d6: 5 + 1 = **6**;",
                "10x[\"rating\" d100 \"weight\" 2d6+1 2d6]"
        );
    }

    @Test
    public void testD66() {
        expect(
                "d66: **15**",
                "d66"
        );
        expect(
                "d666: **152**",
                "d666"
        );
        expect(
                "d6666: **1526**",
                "d6666"
        );
    }

    @Test
    public void testShootingAtVampireExample() {
        expect(
                "shooting at vampire s8: [6; w5] = **6** (success)\n" +
                        "damage 2d6+1: 2 + 6 + 1 = **9**",
                "shooting", "at", "vampire", "s8", "damage", "2d6+1"
        );
    }

    @Test
    public void testDebugMode() {
        expect(
                "*Debug mode enabled.*\n" +
                        "\n" +
                        "shooting\n" +
                        "```\n" +
                        "NonParsedString text='shooting' parserErrorMessage='[1]: token recognition error at: 'h''\n" +
                        "```\n" +
                        "shooting \n" +
                        "2d6\n" +
                        "```\n" +
                        "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: Int 2\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null\n" +
                        "```\n" +
                        "2d6: 1 + 5 = **6**",
                "--debug", "shooting", "2d6"
        );
    }

    @Test
    public void testSuccessOrFail() {
        expect(
                "12d10s7: [successes(6): 10, 10, 9, 8, 8, 8; rest: 6, 5, 4, 2, 2, 1] = **6**",
                "12d10s7"
        );
        expect(
                "12d10s7f1: [successes(6): 10, 10, 9, 8, 8, 8; failures(1): 1; rest: 6, 5, 4, 2, 2] = **5**",
                "12d10s7f1"
        );
        expect(
                "12d10f1s7: [successes(6): 10, 10, 9, 8, 8, 8; failures(1): 1; rest: 6, 5, 4, 2, 2] = **5**",
                "12d10f1s7"
        );
        expect(
                "28d6!s10f1: [successes(4): 33, 28, 23, 11; failures(3): 1, 1, 1; " +
                        "rest: 9, 9, 9, 9, 5, 5, 5, 5, 5, 4, 4, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2] = **1**",
                "28d6!s10f1"
        );
    }

    @Test
    public void testBoundTo() {
        expect(
                "5x5d10[20:30]: \n" +
                        "1: 5d10[20:30]: {1 + 9 + 10 + 8 + 6 = 34=>30} = **30**\n" +
                        "2: 5d10[20:30]: {4 + 2 + 2 + 10 + 5 = 23} = **23**\n" +
                        "3: 5d10[20:30]: {8 + 8 + 4 + 3 + 6 = 29} = **29**\n" +
                        "4: 5d10[20:30]: {5 + 5 + 6 + 2 + 1 = 19=>20} = **20**\n" +
                        "5: 5d10[20:30]: {4 + 9 + 5 + 8 + 3 = 29} = **29**",
                "5x5d10[20:30]"
        );
        expect(
                "5x5d10[20:]: \n" +
                        "1: 5d10[20:]: {1 + 9 + 10 + 8 + 6 = 34} = **34**\n" +
                        "2: 5d10[20:]: {4 + 2 + 2 + 10 + 5 = 23} = **23**\n" +
                        "3: 5d10[20:]: {8 + 8 + 4 + 3 + 6 = 29} = **29**\n" +
                        "4: 5d10[20:]: {5 + 5 + 6 + 2 + 1 = 19=>20} = **20**\n" +
                        "5: 5d10[20:]: {4 + 9 + 5 + 8 + 3 = 29} = **29**",
                "5x5d10[20:]"
        );
        expect(
                "5x5d10[:30]: \n" +
                        "1: 5d10[:30]: {1 + 9 + 10 + 8 + 6 = 34=>30} = **30**\n" +
                        "2: 5d10[:30]: {4 + 2 + 2 + 10 + 5 = 23} = **23**\n" +
                        "3: 5d10[:30]: {8 + 8 + 4 + 3 + 6 = 29} = **29**\n" +
                        "4: 5d10[:30]: {5 + 5 + 6 + 2 + 1 = 19} = **19**\n" +
                        "5: 5d10[:30]: {4 + 9 + 5 + 8 + 3 = 29} = **29**",
                "5x5d10[:30]"
        );
        expect(
                "Error: `5x5d10[:]`: At least one bound should be provided: `5d10[:]`",
                "5x5d10[:]"
        );
        expect(
                "Error: `5d10[30:20]`: Empty range: `5d10[30:20]`",
                "5d10[30:20]"
        );
        expect(
                "5d10[10+20:20]: Empty range in `5d10[10+20:20]`: [30:20]",
                "5d10[10+20:20]"
        );
        expect(
                "(d10!+d8!+d6!)[8:10+8+6]: {(1 + 7 + 2) = 10} = **10**",
                "(d10!+d8!+d6!)[8:10+8+6]"
        );
        expect(
                "(d10!+d8!+d6!)[8:10+8+6]+4: {(1 + 7 + 2) = 10} + 4 = **14**",
                "(d10!+d8!+d6!)[8:10+8+6]+4"
        );
        expect(
                "5x(d10!+d8!+d6!)[8:10+8+6]: \n" +
                        "1: (d10!+d8!+d6!)[8:10+8+6]: {(1 + 7 + 2) = 10} = **10**\n" +
                        "2: (d10!+d8!+d6!)[8:10+8+6]: {(8 + 6 + 16) = 30=>24} = **24**\n" +
                        "3: (d10!+d8!+d6!)[8:10+8+6]: {(15 + 3 + 17) = 35=>24} = **24**\n" +
                        "4: (d10!+d8!+d6!)[8:10+8+6]: {(6 + 17 + 9) = 32=>24} = **24**\n" +
                        "5: (d10!+d8!+d6!)[8:10+8+6]: {(4 + 3 + 3) = 10} = **10**",
                "5x(d10!+d8!+d6!)[8:10+8+6]"
        );
    }

    @Test
    public void testVariables() {
        expect(
                "d20+@dd:=d5: 1 + {@dd=4} = **5**\n" +
                        "@dd+d8: {@dd=4} + 2 = **6**",
                "d20+@dd:=d5", "@dd+d8"
        );
        expect(
                "10x[d10+@dd:=d5; @dd+d8]: \n" +
                        "1: d10+@dd:=d5: 1 + {@dd=4} = **5**; @dd+d8: {@dd=4} + 2 = **6**; \n" +
                        "2: d10+@dd:=d5: 8 + {@dd=1} = **9**; @dd+d8: {@dd=1} + 3 = **4**; \n" +
                        "3: d10+@dd:=d5: 2 + {@dd=2} = **4**; @dd+d8: {@dd=2} + 5 = **7**; \n" +
                        "4: d10+@dd:=d5: 5 + {@dd=3} = **8**; @dd+d8: {@dd=3} + 3 = **6**; \n" +
                        "5: d10+@dd:=d5: 4 + {@dd=3} = **7**; @dd+d8: {@dd=3} + 8 = **11**; \n" +
                        "6: d10+@dd:=d5: 5 + {@dd=5} = **10**; @dd+d8: {@dd=5} + 1 = **6**; \n" +
                        "7: d10+@dd:=d5: 2 + {@dd=1} = **3**; @dd+d8: {@dd=1} + 3 = **4**; \n" +
                        "8: d10+@dd:=d5: 9 + {@dd=5} = **14**; @dd+d8: {@dd=5} + 3 = **8**; \n" +
                        "9: d10+@dd:=d5: 3 + {@dd=1} = **4**; @dd+d8: {@dd=1} + 1 = **2**; \n" +
                        "10: d10+@dd:=d5: 3 + {@dd=3} = **6**; @dd+d8: {@dd=3} + 1 = **4**;",
                "10x[d10+@dd:=d5; @dd+d8]"
        );
        expect(
                "@n:=2d4: {@n=3 + 4} = **7**\n" +
                        "(@n)x[d20; d4]: (@n): ({@n=7}) = **7**\n" +
                        "1: d20: **10**; d4: **3**; \n" +
                        "2: d20: **16**; d4: **2**; \n" +
                        "3: d20: **12**; d4: **1**; \n" +
                        "4: d20: **20**; d4: **4**; \n" +
                        "5: d20: **18**; d4: **2**; \n" +
                        "6: d20: **14**; d4: **3**; \n" +
                        "7: d20: **16**; d4: **4**;",
                "@n:=2d4", "(@n)x[d20; d4]"
        );
    }

    @Test
    public void testSavageWorldsChecksWithSuccessesInExplanation() {
        expect(
                "s8+5: [6; w5] + 5 = **11** (success; **1** raise)",
                "s8+5"
        );
        expect(
                "10xs10: \n" +
                        "1: s10: [1; w5] = **5** (success)\n" +
                        "2: s10: [18; w22] = **22** (success; **4** raises)\n" +
                        "3: s10: [15; w23] = **23** (success; **4** raises)\n" +
                        "4: s10: [6; w3] = **6** (success)\n" +
                        "5: s10: [5; w4] = **5** (success)\n" +
                        "6: s10: [2; w3] = **3**\n" +
                        "7: s10: [4; w1] = **4** (success)\n" +
                        "8: s10: [5; w9] = **9** (success; **1** raise)\n" +
                        "9: s10: [1; w4] = **4** (success)\n" +
                        "10: s10: [3; w1] = **3**",
                "10xs10"
        );
    }

    @Test
    public void testSavageWorldsChecksWithSuccessesInExplanationsWithParenthesizedExpressions() {
        expect(
                "(s8+2): ([6; w5] + 2) = **8** (success; **1** raise)",
                "(s8+2)"
        );
        expect(
                "(s8)+2: ([6; w5]) + 2 = **8** (success; **1** raise)",
                "(s8)+2"
        );
        expect(
                "s8+(2): [6; w5] + (2) = **8** (success; **1** raise)",
                "s8+(2)"
        );
    }

    @Test
    public void testSavageWorldsChecksWithAlternativeTargetNumber() {
        expect(
                "s8t6+4: [6; w5] + 4 = **10** (success; **1** raise)",
                "s8t6+4"
        );
        expect(
                "10xs12t5r5: \n" +
                        "1: s12t5r5: [1; w5] = **5** (success)\n" +
                        "2: s12t5r5: [2; w28] = **28** (success; **4** raises)\n" +
                        "3: s12t5r5: [4; w3] = **4**\n" +
                        "4: s12t5r5: [6; w17] = **17** (success; **2** raises)\n" +
                        "5: s12t5r5: [21; w5] = **21** (success; **3** raises)\n" +
                        "6: s12t5r5: [4; w9] = **9** (success)\n" +
                        "7: s12t5r5: [8; w1] = **8** (success)\n" +
                        "8: s12t5r5: [9; w9] = **9** (success)\n" +
                        "9: s12t5r5: [5; w4] = **5** (success)\n" +
                        "10: s12t5r5: [11; w1] = **11** (success; **1** raise)",
                "10xs12t5r5"
        );
        expect(
                "10xs12tr5: \n" +
                        "1: s12tr5: [1; w5] = **5** (success)\n" +
                        "2: s12tr5: [2; w28] = **28** (success; **4** raises)\n" +
                        "3: s12tr5: [4; w3] = **4**\n" +
                        "4: s12tr5: [6; w17] = **17** (success; **2** raises)\n" +
                        "5: s12tr5: [21; w5] = **21** (success; **3** raises)\n" +
                        "6: s12tr5: [4; w9] = **9** (success)\n" +
                        "7: s12tr5: [8; w1] = **8** (success)\n" +
                        "8: s12tr5: [9; w9] = **9** (success)\n" +
                        "9: s12tr5: [5; w4] = **5** (success)\n" +
                        "10: s12tr5: [11; w1] = **11** (success; **1** raise)",
                "10xs12tr5"
        );
    }

    @Test
    public void testSavageWorldsExtrasRoll() {
        expect(
                "e6: 1 = **1**",
                "e6"
        );
        expect(
                "5e6+4: \n" +
                        "1: e6+4: 1 + 4 = **5** (success)\n" +
                        "2: e6+4: 5 + 4 = **9** (success; **1** raise)\n" +
                        "3: e6+4: 2 + 4 = **6** (success)\n" +
                        "4: e6+4: 28 + 4 = **32** (success; **7** raises)\n" +
                        "5: e6+4: 4 + 4 = **8** (success; **1** raise)",
                "5e6+4"
        );
        expect(
                "5e6t5: \n" +
                        "1: e6t5: 1 = **1**\n" +
                        "2: e6t5: 5 = **5** (success)\n" +
                        "3: e6t5: 2 = **2**\n" +
                        "4: e6t5: 28 = **28** (success; **5** raises)\n" +
                        "5: e6t5: 4 = **4**",
                "5e6t5"
        );
        expect(
                "5e6r7: \n" +
                        "1: e6r7: 1 = **1**\n" +
                        "2: e6r7: 5 = **5** (success)\n" +
                        "3: e6r7: 2 = **2**\n" +
                        "4: e6r7: 28 = **28** (success; **3** raises)\n" +
                        "5: e6r7: 4 = **4** (success)",
                "5e6r7"
        );
        expect(
                "5e6tr6: \n" +
                        "1: e6tr6: 1 = **1**\n" +
                        "2: e6tr6: 5 = **5**\n" +
                        "3: e6tr6: 2 = **2**\n" +
                        "4: e6tr6: 28 = **28** (success; **3** raises)\n" +
                        "5: e6tr6: 4 = **4**",
                "5e6tr6"
        );
        expect(
                "5e6t10+2: \n" +
                        "1: e6t10+2: 1 + 2 = **3**\n" +
                        "2: e6t10+2: 5 + 2 = **7**\n" +
                        "3: e6t10+2: 2 + 2 = **4**\n" +
                        "4: e6t10+2: 28 + 2 = **30** (success; **5** raises)\n" +
                        "5: e6t10+2: 4 + 2 = **6**",
                "5e6t10+2"
        );
        expect(
                "e8+2d6: 6 + 5 + 2 = **13** (success; **2** raises)",
                "e8+2d6"
        );
        expect(
                "e8t5+2d6: 6 + 5 + 2 = **13** (success; **2** raises)",
                "e8t5+2d6"
        );
        expect(
                "2d6+e8: 1 + 5 + 2 = **8** (success; **1** raise)",
                "2d6+e8"
        );
    }

    @Test
    public void testGenericRollsWithTargetNumberAndRaiseStep() {
        expect(
                "2d6t6: 1 + 5 = **6** (shaken)",
                "2d6t6"
        );
        expect(
                "4d6!t6: 1 + 5 + 2 + 28 = **36** (shaken, **7** wounds)",
                "4d6!t6"
        );
        expect(
                "4xd6!t6: \n" +
                        "1: d6!t6: 1 = **1**\n" +
                        "2: d6!t6: 5 = **5**\n" +
                        "3: d6!t6: 2 = **2**\n" +
                        "4: d6!t6: 28 = **28** (shaken, **5** wounds)",
                "4xd6!t6"
        );
        expect(
                "d8!t6+2d6: 6 + 5 + 2 = **13** (shaken, **1** wound)",
                "d8!t6+2d6"
        );
        expect(
                "d8!r6+2d6: 6 + 5 + 2 = **13** (shaken, **1** wound)",
                "d8!r6+2d6"
        );
        expect(
                "d8!tr6+2d6: 6 + 5 + 2 = **13** (shaken, **1** wound)",
                "d8!tr6+2d6"
        );
    }

    @Test
    public void testTargetNumberAndRaiseExpressionPrefix() {
        expect(
                "t6:2d6: 1 + 5 = **6** (shaken)",
                "t6:2d6"
        );
        expect(
                "t6:2d6+1: 1 + 5 + 1 = **7** (shaken)",
                "t6:2d6+1"
        );
        expect(
                "t6:2d6+d4+1: 1 + 5 + 1 + 1 = **8** (shaken)",
                "t6:2d6+d4+1"
        );
        expect(
                "t6:s8+2: [6; w5] + 2 = **8** (success)",
                "t6:s8+2"
        );
        expect(
                "5xtr6:s6: \n" +
                        "1: tr6:s6: [1; w5] = **5**\n" +
                        "2: tr6:s6: [2; w28] = **28** (success; **3** raises)\n" +
                        "3: tr6:s6: [4; w3] = **4**\n" +
                        "4: tr6:s6: [23; w9] = **23** (success; **2** raises)\n" +
                        "5: tr6:s6: [5; w4] = **5**",
                "5xtr6:s6"
        );
        // Only last target number and raise step are taken into account
        expect(
                "t6:s6+t8:s8: [1; w5] + [2; w28] = **33** (success; **6** raises)",
                "t6:s6+t8:s8"
        );
    }

    @Test
    public void testSwithTRPrefix() {
        expect(
                "5xtr6:s10: \n" +
                        "1: tr6:s10: [1; w5] = **5**\n" +
                        "2: tr6:s10: [18; w22] = **22** (success; **2** raises)\n" +
                        "3: tr6:s10: [15; w23] = **23** (success; **2** raises)\n" +
                        "4: tr6:s10: [6; w3] = **6** (success)\n" +
                        "5: tr6:s10: [5; w4] = **5**",
                "5xtr6:s10"
        );
    }

    @Test
    public void testEwithTRPrefix() {
        expect(
                "5xtr6:e10: \n" +
                        "1: tr6:e10: 1 = **1**\n" +
                        "2: tr6:e10: 9 = **9** (success)\n" +
                        "3: tr6:e10: 18 = **18** (success; **2** raises)\n" +
                        "4: tr6:e10: 6 = **6** (success)\n" +
                        "5: tr6:e10: 4 = **4**",
                "5xtr6:e10"
        );
    }

    @Test
    public void testMultipleSavageWorldChecksWithTargetNumbers() {
        // Only last TN and raise step is taken into account
        expect(
                "s8t4+s8t6: [6; w5] + [2; w28] = **34** (success; **7** raises)",
                "s8t4+s8t6"
        );
    }

    @Test
    public void testBracketsInExplanationOfSumRollMultipliedBySomething() {
        expect(
                "3*3d6: 3 * (1 + 5 + 2) = **24**",
                "3*3d6"
        );
        expect(
                "3d6*3: (1 + 5 + 2) * 3 = **24**",
                "3d6*3"
        );
        expect(
                "3d6*3d6: (1 + 5 + 2) * (6 + 6 + 6) = **144**",
                "3d6*3d6"
        );
    }

    @Test
    public void testCarcosaRolls() {
        expect(
                "dC: 4 = **4**",
                "dC"
        );
        expect(
                "4dc: 4 + 1 + 3 + 3 = **11**",
                "4dc"
        );
        expect(
                "2dc*4: (4 + 1) * 4 = **20**",
                "2dc*4"
        );
        expect(
                "4*2DC: 4 * (4 + 1) = **20**",
                "4*2DC"
        );
        expect(
                "10x2dc: \n" +
                        "1: 2dc: 4 + 1 = **5**\n" +
                        "2: 2dc: 6 + 6 = **12**\n" +
                        "3: 2dc: 1 + 5 = **6**\n" +
                        "4: 2dc: 8 + 8 = **16**\n" +
                        "5: 2dc: 3 + 6 = **9**\n" +
                        "6: 2dc: 5 + 4 = **9**\n" +
                        "7: 2dc: 1 + 2 = **3**\n" +
                        "8: 2dc: 2 + 3 = **5**\n" +
                        "9: 2dc: 1 + 4 = **5**\n" +
                        "10: 2dc: 3 + 1 = **4**",
                "10x2dc"
        );
    }

    @Test
    public void testD100AsPercent() {
        expect(
                "10d%: 61 + 49 + 30 + 48 + 16 + 54 + 92 + 62 + 20 + 55 = **487**",
                "10d%"
        );
    }

    @Test
    public void testWegD6() {
        expect("0w: Dice count should be at least 1: 0", "0w");
        expect("1w: ~~1~~ = **0**", "1w");
        expect("5w: w1; 5 + 2 + ~~6~~ + 6 = **13**", "5w");
        expect("4w+2: w1; 5 + 2 + ~~6~~ + 2 = **9**", "4w+2");
        expect("4w*2: (w1; 5 + 2 + ~~6~~) * 2 = **14**", "4w*2");
        expect(
                "10x4w: \n" +
                        "1: 4w: w1; 5 + 2 + ~~6~~ = **7**\n" +
                        "2: 4w: w22 + 4 + 3 + 6 = **35**\n" +
                        "3: 4w: w17 + 6 + 3 + 5 = **31**\n" +
                        "4: 4w: w4 + 6 + 3 + 2 = **15**\n" +
                        "5: 4w: w1; 3 + ~~6~~ + 3 = **6**\n" +
                        "6: 4w: w5 + 4 + 5 + 1 = **15**\n" +
                        "7: 4w: w33 + 3 + 4 + 6 = **46**\n" +
                        "8: 4w: w5 + 4 + 3 + 2 = **14**\n" +
                        "9: 4w: w9 + 5 + 4 + 5 = **23**\n" +
                        "10: 4w: w5 + 5 + 1 + 2 = **13**",
                "10x4w"
        );
    }

    @Test
    public void testGenericTargetNumber() {
        expect(
                "4xtn10:2d10!+1: \n" +
                        "1: tn10:2d10!+1: 1 + 9 + 1 = **11** (success, MoS=1)\n" +
                        "2: tn10:2d10!+1: 18 + 6 + 1 = **25** (success, MoS=15)\n" +
                        "3: tn10:2d10!+1: 4 + 2 + 1 = **7** (failure, MoF=3)\n" +
                        "4: tn10:2d10!+1: 2 + 15 + 1 = **18** (success, MoS=8)",
                "4xtn10:2d10!+1"
        );
        expect(
                "4xTN10:2d10!+1: \n" +
                        "1: TN10:2d10!+1: 1 + 9 + 1 = **11** (success, MoS=1)\n" +
                        "2: TN10:2d10!+1: 18 + 6 + 1 = **25** (success, MoS=15)\n" +
                        "3: TN10:2d10!+1: 4 + 2 + 1 = **7** (failure, MoF=3)\n" +
                        "4: TN10:2d10!+1: 2 + 15 + 1 = **18** (success, MoS=8)",
                "4xTN10:2d10!+1"
        );
        expect(
                "4xtn10+:2d10!+1: \n" +
                        "1: tn10+:2d10!+1: 1 + 9 + 1 = **11** (success, MoS=1)\n" +
                        "2: tn10+:2d10!+1: 18 + 6 + 1 = **25** (success, MoS=15)\n" +
                        "3: tn10+:2d10!+1: 4 + 2 + 1 = **7** (failure, MoF=3)\n" +
                        "4: tn10+:2d10!+1: 2 + 15 + 1 = **18** (success, MoS=8)",
                "4xtn10+:2d10!+1"
        );
        expect(
                "4xtn10-:3d6: \n" +
                        "1: tn10-:3d6: 1 + 5 + 2 = **8** (success, MoS=2)\n" +
                        "2: tn10-:3d6: 6 + 6 + 6 = **18** (failure, MoF=8)\n" +
                        "3: tn10-:3d6: 6 + 4 + 4 = **14** (failure, MoF=4)\n" +
                        "4: tn10-:3d6: 3 + 6 + 6 = **15** (failure, MoF=5)",
                "4xtn10-:3d6"
        );
    }

    @Test
    public void testIronSwornRoll() {
        expect("**Miss**: 1 VS 9, 10", "i");
        expect("**Miss**: 1 + 2 = 3 VS 9, 10", "i+2");
        expect("**Miss**: 1 - 2 = -1 VS 9, 10", "i-2");
        expect("**Strong hit**: 1 + 10 = 11 VS 9, 10", "i+10");
        expect("**Weak hit**: 1 + 8 = 9 VS 9, 10", "i+8");
    }

    private void expect(String expectedResult, String... args) {
        List<Statement> statements = new Parser().parse(args);
        CommandContext context = new CommandContext(new Random(0));
        RollInterpreter interpreter = new RollInterpreter(context);
        String actualResult = TestUtils.normalize(interpreter.run(statements));
        String expected = TestUtils.normalize(expectedResult);
        Assert.assertEquals(expected, actualResult);
    }
}
