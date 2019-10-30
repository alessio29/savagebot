package tests;

import org.alessio29.savagebot.commands.info.InfoCommands;
import org.alessio29.savagebot.internal.commands.CommandRegistry;
import org.alessio29.savagebot.internal.commands.Commands;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TestCommandsRegistry {
    @Before
    public void setup() {
        Commands.registerDefaultCommands();
    }

    @Test
    public void testDefaultCommandsAreRegistered() {
        List<String> commands = new HashSet<>(CommandRegistry.getInstance().getRegisteredCommands())
                .stream()
                .map(cmd -> cmd.getCategory() + ":" + cmd.getName())
                .sorted()
                .collect(Collectors.toList());
        Assert.assertEquals(
                "[" +
                        "ADMIN:ping, ADMIN:prefix, " +
                        "BENNIES:benny, BENNIES:hat, BENNIES:pocket, BENNIES:use, " +
                        "CARDS:deal, CARDS:open, CARDS:show, CARDS:shuffle, " +
                        "DICE:r, DICE:rs, " +
                        "INFO:help, INFO:invite, " +
                        "INITIATIVE:draw, INITIATIVE:fight, INITIATIVE:init, INITIATIVE:round, " +
                        "TOKENS:clear, TOKENS:give, TOKENS:take, TOKENS:tokens" +
                        "]",
                commands.toString()
        );
    }

    @Test
    public void testHelpText() {
        Assert.assertEquals(
                "\n" +
                        "__**CARDS category**__\n" +
                        "\n" +
                        "**deal**\tCardCount User\tsecretly deals n (1 by default) cards to user (to self by default)\n" +
                        "\n" +
                        "**open**\t\topenly deals several (1 by default) cards to current channel\n" +
                        "\n" +
                        "**show**\t\tShows your cards, previously dealt to you by 'deal' command to current channel\n" +
                        "\n" +
                        "**shuffle**\t\tShuffles current deck, resets secret cards dealt to all users in this channel\n" +
                        "\n" +
                        "__**DICE category**__\n" +
                        "\n" +
                        "**r**\t<expression1> ... <expressionN> \trolls dice.\n" +
                        "\n" +
                        "**rs**\t[<heading_1>] <expression_1> ... [<heading_N>] <expression_N>\trolls multiple dice and print them out sorted.\n" +
                        "\n" +
                        "__**BENNIES category**__\n" +
                        "\n" +
                        "**benny**\t<character>\tGet benny from hat and adds it to characker's pocket\n" +
                        "\n" +
                        "**hat**\t[fill]\tPuts all required bennies into the hat\n" +
                        "\n" +
                        "**pocket**\t<characterName>\tShows character's bennies\n" +
                        "\n" +
                        "**use**\t<BennyColor> <CharacterName>\tUses one of character's benny\n" +
                        "\n" +
                        "__**INITIATIVE category**__\n" +
                        "\n" +
                        "**draw**\tcharacter [ilqh]\tdraws card to character\n" +
                        "\n" +
                        "**fight**\t\tstarts new fight: shuffles deck, resets intiative tracker\n" +
                        "\n" +
                        "**init**\t\tShows initiative tracker\n" +
                        "\n" +
                        "**round**\t\tStarts new round: resets resets intiative tracker, shuffles deck, if joker was dealt on previous round\n" +
                        "\n" +
                        "__**INFO category**__\n" +
                        "\n" +
                        "**help**\t[<command> or <category>]\tLists the description and syntax for registered commands.\n" +
                        "\n" +
                        "**invite**\t\tCreates invite link for this bot\n" +
                        "\n" +
                        "__**ADMIN category**__\n" +
                        "\n" +
                        "**ping**\t\tChecks SavageBot readiness\n" +
                        "\n" +
                        "**prefix**\t[<character>]\tSets <character> as custom user-defined command prefix or shows current prefix\n" +
                        "\n" +
                        "__**TOKENS category**__\n" +
                        "\n" +
                        "**clear**\t<character_name>/all\tClears tokens for named character or all characters in current channel \n" +
                        "\n" +
                        "**give**\t<character_name> [<amount of tokens>]\tGives character token (benny, Fate point etc..)\n" +
                        "\n" +
                        "**take**\t<character_name> [<amount of tokens>]\tTakes tokens (benny, Fate point etc..) from  character \n" +
                        "\n" +
                        "**tokens**\t\tList characters and their tokens\n",
                InfoCommands.getHelpForAllCommands()
        );
    }
}
