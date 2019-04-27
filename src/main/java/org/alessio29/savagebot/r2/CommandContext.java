package org.alessio29.savagebot.r2;

import java.util.Random;

public class CommandContext {
    private final Random random;

    public CommandContext(Random random) {
        this.random = random;
    }

    public Random getRandom() {
        return random;
    }
}
