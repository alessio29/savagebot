package org.alessio29.savagebot.commands.dice;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.commands.Category;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.dice.Dice;
import org.alessio29.savagebot.internal.CommandExecutionResult;
import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.Interpreter;
import org.alessio29.savagebot.r2.parse.Parser;

public class RollDice2Command implements ICommand {
    @Override
    public String getName() {
        return "rr";
    }

    @Override
    public Category getCategory() {
        return Category.DICE;
    }

    @Override
    public String getDescription() {
        return "rolls dice. Understands complex expressions.\n" +
                "Multiple die rolls in a single command: `.rr 2d6+d4+2 d12 d6!`\n" +
                "Repeated die rolls: `.rr 6x4d6k3`\n" +
                "Inline comments: `.rr shooting s8 damage 2d6+1`";
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
    public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
        if (args.length < 1) {
            return new CommandExecutionResult("No commands", args.length + 1);
        }

        String result = new Interpreter(getCommandContext(event)).run(new Parser().parse(args));

        return new CommandExecutionResult(result, args.length + 1);
    }

    private CommandContext getCommandContext(MessageReceivedEvent event) {
        return new CommandContext(Dice.RANDOM);
    }
}
