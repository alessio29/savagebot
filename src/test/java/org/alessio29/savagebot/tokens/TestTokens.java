package org.alessio29.savagebot.tokens;

import org.alessio29.savagebot.apiActions.tokens.ClearTokensAction;
import org.alessio29.savagebot.apiActions.tokens.GiveTokenAction;
import org.alessio29.savagebot.apiActions.tokens.ListTokensAction;
import org.alessio29.savagebot.apiActions.tokens.TakeTokenAction;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.junit.Assert;
import org.junit.Test;


public class TestTokens {
    private static final String TEST_GUILD_ID = "testGuildId";
    private static final String TEST_CHANNEL_ID = "testChannelId";
    private static final String TEST_USER_ID = "testUserId";
    private static final String TEST_CHAR1 = "Test1";
    private static final String TEST_CHAR2 = "Test2";
    private static final String TEST_CHAR3 = "Test3";

    @Test
    public void testTokensScenario () {

        CommandExecutionResult result = new ListTokensAction().doAction(TEST_GUILD_ID, TEST_CHANNEL_ID);
        Assert.assertEquals("No characters with tokens defined!", result.getResult());

        // give
        CommandExecutionResult afterGiveResult1 = new GiveTokenAction().
                doAction(TEST_GUILD_ID, TEST_CHANNEL_ID, new String[] {TEST_CHAR1, "1"});
        Assert.assertEquals("1 token(s) given to character Test1", afterGiveResult1.getResult());

        CommandExecutionResult afterGiveResult2 = new GiveTokenAction().
                doAction(TEST_GUILD_ID, TEST_CHANNEL_ID, new String[] {TEST_CHAR2});
        Assert.assertEquals("1 token(s) given to character Test2", afterGiveResult2.getResult());

        CommandExecutionResult afterGiveResult3 = new GiveTokenAction().
                doAction(TEST_GUILD_ID, TEST_CHANNEL_ID, new String[] {TEST_CHAR3, "4"});
        Assert.assertEquals("4 token(s) given to character Test3", afterGiveResult3.getResult());

        //take
        CommandExecutionResult afterTakeResult1 = new TakeTokenAction().
                doAction(TEST_USER_ID, TEST_GUILD_ID, TEST_CHANNEL_ID, new String[]{TEST_CHAR3, "1"});
        Assert.assertEquals("1 token(s) taken from character Test3", afterTakeResult1.getResult());

        CommandExecutionResult result1 = new ListTokensAction().doAction(TEST_GUILD_ID, TEST_CHANNEL_ID);
        Assert.assertEquals("\n" +
                "```NAME           \tTOKENS\n" +
                "Test1          \t1    \n" +
                "Test3          \t3    \n" +
                "Test2          \t1    \n" +
                "```", result1.getResult());

        CommandExecutionResult afterTakeResult2 = new TakeTokenAction().
                doAction(TEST_USER_ID, TEST_GUILD_ID, TEST_CHANNEL_ID, new String[]{TEST_CHAR3, "2"});
        Assert.assertEquals("2 token(s) taken from character Test3", afterTakeResult2.getResult());

        CommandExecutionResult result2 = new ListTokensAction().doAction(TEST_GUILD_ID, TEST_CHANNEL_ID);
        Assert.assertEquals("\n" +
                "```NAME           \tTOKENS\n" +
                "Test1          \t1    \n" +
                "Test3          \t1    \n" +
                "Test2          \t1    \n" +
                "```", result2.getResult());

        CommandExecutionResult afterGiveResult4 = new GiveTokenAction().
                doAction(TEST_GUILD_ID, TEST_CHANNEL_ID, new String[] {TEST_CHAR1, "2"});
        Assert.assertEquals("2 token(s) given to character Test1", afterGiveResult4.getResult());

        CommandExecutionResult result3 = new ListTokensAction().doAction(TEST_GUILD_ID, TEST_CHANNEL_ID);
        Assert.assertEquals("\n" +
                "```NAME           \tTOKENS\n" +
                "Test1          \t3    \n" +
                "Test3          \t1    \n" +
                "Test2          \t1    \n" +
                "```", result3.getResult());

        CommandExecutionResult afterClearResult1 = new ClearTokensAction().
                doAction(TEST_USER_ID, TEST_GUILD_ID, TEST_CHANNEL_ID, new String[] {TEST_CHAR1});
        Assert.assertEquals("Removed character Test1", afterClearResult1.getResult());

        CommandExecutionResult resultAfterClearChar = new ListTokensAction().doAction(TEST_GUILD_ID, TEST_CHANNEL_ID);
        Assert.assertEquals("\n" +
                "```NAME           \tTOKENS\n" +
                "Test3          \t1    \n" +
                "Test2          \t1    \n" +
                "```", resultAfterClearChar.getResult());


        CommandExecutionResult afterClearResult2 =new ClearTokensAction().
                doAction(TEST_USER_ID, TEST_GUILD_ID, TEST_CHANNEL_ID, new String[] {"all"});
        Assert.assertEquals("Removed all characters", afterClearResult2.getResult());

        CommandExecutionResult resultAfterClearAll = new ListTokensAction().doAction(TEST_GUILD_ID, TEST_CHANNEL_ID);
        Assert.assertEquals("No characters with tokens defined!", resultAfterClearAll.getResult());

    }
}
