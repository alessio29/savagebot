package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.admin.HelpAction;
import org.alessio29.savagebot.internal.commands.CommandRegistry;
import org.alessio29.savagebot.internal.commands.Commands;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Ignore
public class TestCommandsRegistry {
    @Before
    public void setup() {
        CommandRegistry.getInstance().reset();
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
                "[ADMIN:info, ADMIN:ping, ADMIN:prefix, " +
                        "BENNIES:addbenny, BENNIES:clearbennies, BENNIES:givebenny, BENNIES:initbennies, BENNIES:pullbenny, BENNIES:setbennymode, BENNIES:takebenny, " +
                        "CARDS:deal, CARDS:put, CARDS:show, CARDS:shuffle, " +
                        "CHARACTERS:list, CHARACTERS:remove, " +
                        "DICE:ept, DICE:r, DICE:rh, DICE:rs, " +
                        "INFO:help, INFO:invite, " +
                        "INITIATIVE:card, INITIATIVE:deal, INITIATIVE:drop, INITIATIVE:fight, INITIATIVE:hold, INITIATIVE:init, INITIATIVE:round, " +
                        "MUSIC:join, MUSIC:leave, MUSIC:nowplaying, MUSIC:play, MUSIC:queue, MUSIC:skip, MUSIC:stop, " +
                        "STATES:state, " +
                        "TOKENS:clear, TOKENS:give, TOKENS:take]",
                commands.toString()
        );
    }

    @Test
    public void testHelpText() {
        Assert.assertEquals(
                "__**CARDS category**__\n" +
                        "!deal [<card_count>]; aliases: !dl\n" +
                        "!put [<card_count>]\n" +
                        "!show; aliases: !sh\n" +
                        "!shuffle\n" +
                        "\n" +
                        "__**CHARACTERS category**__\n" +
                        "!list\n" +
                        "!remove; aliases: !rm\n" +
                        "\n" +
                        "__**DICE category**__\n" +
                        "!ept <level> <hit_die_expression>\n" +
                        "!r <expression1> ... <expressionN> \n" +
                        "!rh <expression_1> ... <expressionN>\n" +
                        "!rs [<heading_1>] <expression_1> ... [<heading_N>] <expression_N>\n" +
                        "\n" +
                        "__**BENNIES category**__\n" +
                        "!addbenny w/b/r/g; aliases: !ab\n" +
                        "!clearbennies <character1_name>/all [<character2_name>]; aliases: !cb\n" +
                        "!givebenny <character_name> [<amount>]; aliases: !gb\n" +
                        "!initbennies; aliases: !ib\n" +
                        "!pullbenny <character_name> [<amount>]; aliases: !pb\n" +
                        "!setbennymode normal/deadlands; aliases: !sbm\n" +
                        "!takebenny <character_name> [<bennyColor>]; aliases: !tb\n" +
                        "\n" +
                        "__**INITIATIVE category**__\n" +
                        "!card <character_name>; aliases: !cd\n" +
                        "!deal <character_name1> [<modifiers_1>] ... <character_nameN> [<modifiers_N>]; aliases: !di\n" +
                        "!drop <character_name>\n" +
                        "!fight; aliases: !f\n" +
                        "!hold [-]<character>\n" +
                        "!init\n" +
                        "!round [+] [-<char_name>]; aliases: !rd\n" +
                        "\n" +
                        "__**INFO category**__\n" +
                        "!help [<command> or <category>]\n" +
                        "!invite\n" +
                        "\n" +
                        "__**ADMIN category**__\n" +
                        "!info\n" +
                        "!ping\n" +
                        "!prefix [<character>]\n" +
                        "\n" +
                        "__**TOKENS category**__\n" +
                        "!clear <character_name>/all\n" +
                        "!give <character_name> [<amount of tokens>]\n" +
                        "!take <character_name> [<amount of tokens>]\n" +
                        "\n" +
                        "__**STATES category**__\n" +
                        "!state <character_name> [clear] [+/-]<state1> [<state2>] [...]; aliases: !st\n" +
                        "\n" +
                        "__**MUSIC category**__\n" +
                        "!join; aliases: !jn\n" +
                        "!leave; aliases: !lv\n" +
                        "!nowplaying; aliases: !np\n" +
                        "!play music_URL; aliases: !pl\n" +
                        "!queue; aliases: !que\n" +
                        "!skip\n" +
                        "!stop\n" +
                        "\n" +
                        "For more details, use `!help <command>` or see https://github.com/alessio29/savagebot/blob/master/README.md",
                HelpAction.getBriefHelpForAllCommands()
        );
    }

    @Test
    public void testRegisteredParsingCommands() {
        List<String> commands = new HashSet<>(CommandRegistry.getInstance().getRegisteredParsingCommands())
                .stream()
                .map(Object::toString)
                .sorted()
                .collect(Collectors.toList());
        //noinspection ArraysAsListWithZeroOrOneArgument
        Assert.assertEquals(
                Arrays.asList(
                        "ParsingMethodCommand{ method: org.alessio29.savagebot.commands.DiceCommands::parseAndRollDice; methodOwner: null}"
                ),
                commands
        );
    }
}
