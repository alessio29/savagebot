package tests;

import org.alessio29.savagebot.internal.commands.CommandRegistry;
import org.alessio29.savagebot.internal.commands.Commands;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TestCommandsRegistry {
    @Test
    public void testDefaultCommandsAreRegistered() {
        Commands.registerDefaultCommands();
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
}
