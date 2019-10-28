package org.alessio29.savagebot.r2.eval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CommandContext {
    public static final Random RANDOM = new Random();

    private final Random random;
    private final Map<String, List<Integer>> variables = new HashMap<>();

    public CommandContext(Random random) {
        this.random = random;
    }

    public CommandContext() {
        this(RANDOM);
    }

    public Random getRandom() {
        return random;
    }

    public void putVariable(String variable, List<Integer> value) {
        variables.put(variable, value);
    }

    public List<Integer> getVariable(String variable) {
        return variables.get(variable);
    }
}
