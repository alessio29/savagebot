package tests;

import org.alessio29.savagebot.commands.InfoCommands;
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
                        "ADMIN:info, ADMIN:ping, ADMIN:prefix, " +
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
                "__**CARDS category**__\n" +
                        "!deal CardCount User\n" +
                        "!open\n" +
                        "!show\n" +
                        "!shuffle\n" +
                        "\n" +
                        "__**DICE category**__\n" +
                        "!r <expression1> ... <expressionN> \n" +
                        "!rs [<heading_1>] <expression_1> ... [<heading_N>] <expression_N>\n" +
                        "\n" +
                        "__**BENNIES category**__\n" +
                        "!benny <character>\n" +
                        "!hat [fill]\n" +
                        "!pocket <characterName>\n" +
                        "!use <BennyColor> <CharacterName>\n" +
                        "\n" +
                        "__**INITIATIVE category**__\n" +
                        "!draw character [ilqh]\n" +
                        "!fight\n" +
                        "!init\n" +
                        "!round\n" +
                        "\n" +
                        "__**INFO category**__\n" +
                        "!help [<command> or <category>]\n" +
                        "!invite\n" +
                        "\n" +
                        "__**ADMIN category**__\n" +
                        "!info password\n" +
                        "!ping\n" +
                        "!prefix [<character>]\n" +
                        "\n" +
                        "__**TOKENS category**__\n" +
                        "!clear <character_name>/all\n" +
                        "!give <character_name> [<amount of tokens>]\n" +
                        "!take <character_name> [<amount of tokens>]\n" +
                        "!tokens\n" +
                        "\n" +
                        "For more details, use `!help <command>` or see https://github.com/alessio29/savagebot/blob/master/README.md",
                InfoCommands.getBriefHelpForAllCommands()
        );
    }
}
