package org.alessio29.savagebot.commands.dice;

import org.alessio29.savagebot.commands.Category;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.commands.IParsingCommand;
import org.alessio29.savagebot.dice.Dice;
import org.alessio29.savagebot.internal.CommandExecutionResult;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.Interpreter;
import org.alessio29.savagebot.r2.parse.Parser;
import org.alessio29.savagebot.r2.tree.NonParsedStringStatement;
import org.alessio29.savagebot.r2.tree.Statement;

import java.util.List;


public class RollDice2Command implements ICommand, IParsingCommand {
    @Override
    public String getName() {
        return "r";
    }

    @Override
    public Category getCategory() {
        return Category.DICE;
    }

    @Override
    public String getDescription() {
        return "rolls dice. Understands complex expressions.\n" +
                "Multiple die rolls in a single command: `!r 2d6+d4+2 d12 d6!`\n" +
                "Repeated die rolls: `!r 6x4d6k3`\n" +
                "Inline comments: `!r shooting s8 damage 2d6+1`" +
                "Just roll them bones (no spaces in expressions): `shooting !s8 damage !2d6+1`";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String[] getArguments() {
        return new String[] { "<expression1> ... <expressionN> "};
    }

    @Override
    public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("No commands", args.length + 1);
        }

        String result = new Interpreter(getCommandContext()).run(new Parser().parse(args));

        return new CommandExecutionResult(result, args.length + 1);
    }

    @Override
    public CommandExecutionResult parseAndExecuteOrNull(MessageReceivedEvent event, String command) {
        List<Statement> statements = new Parser().parseCommandElement(command);
        if (statements.stream().allMatch(statement -> statement instanceof NonParsedStringStatement)) {
            return null;
        }

        String result = new Interpreter(getCommandContext()).run(statements);

        return new CommandExecutionResult(result, 1);
    }

    private CommandContext getCommandContext() {
        return new CommandContext(Dice.RANDOM);
    }
}
