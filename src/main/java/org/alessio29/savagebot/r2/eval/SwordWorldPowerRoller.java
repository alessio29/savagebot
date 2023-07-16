package org.alessio29.savagebot.r2.eval;

import java.util.StringJoiner;

public class SwordWorldPowerRoller {
    private final Roller roller;

    private int power;
    private int critical;
    private int autoFailThreshold;
    private int numDice;
    private int rollModifier;
    private boolean withHumanSwordGrace = false;

    public SwordWorldPowerRoller(Roller roller) {
        this.roller = roller;
    }

    public SwordWorldPowerRoller power(int power) {
        this.power = power;
        return this;
    }

    public SwordWorldPowerRoller critical(int critical) {
        this.critical = critical;
        return this;
    }

    public SwordWorldPowerRoller autoFailThreshold(int autoFailThreshold) {
        this.autoFailThreshold = autoFailThreshold;
        return this;
    }

    public SwordWorldPowerRoller numDice(int numDice) {
        this.numDice = numDice;
        return this;
    }

    public SwordWorldPowerRoller rollModifier(int rollModifier) {
        this.rollModifier = rollModifier;
        return this;
    }

    public SwordWorldPowerRoller withHumanSwordGrace(boolean withHumanSwordGrace) {
        this.withHumanSwordGrace = withHumanSwordGrace;
        return this;
    }

    public static final class Result {
        private final boolean isAutoFail;
        private final int value;
        private final String explained;

        public Result(boolean isAutoFail, int value, String explained) {
            this.isAutoFail = isAutoFail;
            this.value = value;
            this.explained = explained;
        }

        public boolean isAutoFail() {
            return isAutoFail;
        }

        public int getValue() {
            return value;
        }

        public String getExplained() {
            return explained;
        }
    }

    public Result roll() {
        if (power < 0 || power > MAX_POWER) {
            throw new EvaluationErrorException("Power out of range [0.." + MAX_POWER + "]: " + power);
        }

        if (critical > 0 && critical <= 5) {
            // Critical <= 0 means there's no critical.
            // Critical 1..2 would roll forever.
            // Critical 3..5 is not possible under core rules (and can potentially roll for a very long time).
            throw new EvaluationErrorException("Critical out of range: " + critical);
        }

        if (autoFailThreshold < 0 || autoFailThreshold > 12) {
            // Allow 'f0' to signify that automatic fail on this roll is impossible (for whatever reason)
            throw new EvaluationErrorException("Automatic fail threshold out of range: " + autoFailThreshold);
        }

        if (numDice < 1 || numDice > 10) {
            // It should really be 1 or 2
            throw new EvaluationErrorException("Number of dice out of range: " + numDice);
        }

        int total = 0;
        StringBuilder addends = new StringBuilder();
        boolean isAutoFail = false;
        boolean isFirstRoll = true;

        while (true) {
            PowerTableRollResult ptr = rollOnTable();

            if (ptr.roll <= autoFailThreshold) {
                if (isFirstRoll) {
                    isAutoFail = true;
                }
                addends.append("\\*");
                break;
            }

            isFirstRoll = false;

            total += ptr.value;
            addends.append(ptr.value);
            if (withHumanSwordGrace) {
                addends.append("[").append(ptr.rollText).append("]");
            }
            if (critical <= 0 || ptr.roll < critical) {
                break;
            } else {
                addends.append(" + ");
            }
        }

        StringBuilder explained = new StringBuilder();
        explained.append("(");
        if (withHumanSwordGrace) {
            explained.append(getPowerTableAsString(power));
        } else {
            explained.append("power ").append(power);
        }
        explained.append("; ");
        if (critical <= 0) {
            explained.append("no critical");
        } else {
            explained.append("critical ").append(critical);
        }
        explained.append(") ").append(addends);

        return new Result(isAutoFail, total, explained.toString());
    }

    private static class PowerTableRollResult {
        public final int roll;
        public final int value;
        public final String rollText;

        public PowerTableRollResult(int roll, int value, String rollText) {
            this.roll = roll;
            this.value = value;
            this.rollText = rollText;
        }
    }

    private PowerTableRollResult rollOnTable() {
        int[] table = POWER_TABLE[power];

        IntResult rollResult = roller.roll(numDice, 6);

        int rollValue = rollResult.getValue();
        int modifiedRoll = rollValue + rollModifier;
        if (numDice == 2 && rollValue == 2) {
            modifiedRoll = rollValue;
        }

        int tableIndex = modifiedRoll - 2;
        if (tableIndex < 0) {
            tableIndex = 0;
        } else if (tableIndex >= table.length) {
            tableIndex = table.length - 1;
        }

        StringBuilder rollText = new StringBuilder(rollResult.getExplained());
        if (rollModifier > 0) {
            rollText.append(" + ").append(rollModifier);
        } else if (rollModifier < 0){
            rollText.append(" - ").append(-rollModifier);
        }

        return new PowerTableRollResult(modifiedRoll, table[tableIndex], rollText.toString());
    }

    private static String getPowerTableAsString(int power) {
        int[] table = POWER_TABLE[power];
        StringJoiner joiner = new StringJoiner("|");
        for (int i = 1; i < table.length; ++i) {
            joiner.add(Integer.toString(table[i]));
        }
        return joiner.toString();
    }

    // Generated from tables in Sword World Core Rulebook III, '-1' stands for '*'
    private static final int[][] POWER_TABLE = new int[][]{
            {-1, 0, 0, 0, 1, 2, 2, 3, 3, 4, 4},
            // 1
            {-1, 0, 0, 0, 1, 2, 3, 3, 3, 4, 4},
            {-1, 0, 0, 0, 1, 2, 3, 4, 4, 4, 4},
            {-1, 0, 0, 1, 1, 2, 3, 4, 4, 4, 5},
            {-1, 0, 0, 1, 2, 2, 3, 4, 4, 5, 5},
            {-1, 0, 1, 1, 2, 2, 3, 4, 5, 5, 5},
            {-1, 0, 1, 1, 2, 3, 3, 4, 5, 5, 5},
            {-1, 0, 1, 1, 2, 3, 4, 4, 5, 5, 6},
            {-1, 0, 1, 2, 2, 3, 4, 4, 5, 6, 6},
            {-1, 0, 1, 2, 3, 3, 4, 4, 5, 6, 7},
            {-1, 1, 1, 2, 3, 3, 4, 5, 5, 6, 7},
            // 11
            {-1, 1, 2, 2, 3, 3, 4, 5, 6, 6, 7},
            {-1, 1, 2, 2, 3, 4, 4, 5, 6, 6, 7},
            {-1, 1, 2, 3, 3, 4, 4, 5, 6, 7, 7},
            {-1, 1, 2, 3, 4, 4, 4, 5, 6, 7, 8},
            {-1, 1, 2, 3, 4, 4, 5, 5, 6, 7, 8},
            {-1, 1, 2, 3, 4, 4, 5, 6, 7, 7, 8},
            {-1, 1, 2, 3, 4, 5, 5, 6, 7, 7, 8},
            {-1, 1, 2, 3, 4, 5, 6, 6, 7, 7, 8},
            {-1, 1, 2, 3, 4, 5, 6, 7, 7, 8, 9},
            {-1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
            // 21
            {-1, 1, 2, 3, 4, 6, 6, 7, 8, 9, 10},
            {-1, 1, 2, 3, 5, 6, 6, 7, 8, 9, 10},
            {-1, 2, 2, 3, 5, 6, 7, 7, 8, 9, 10},
            {-1, 2, 3, 4, 5, 6, 7, 7, 8, 9, 10},
            {-1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10},
            {-1, 2, 3, 4, 5, 6, 8, 8, 9, 9, 10},
            {-1, 2, 3, 4, 6, 6, 8, 8, 9, 9, 10},
            {-1, 2, 3, 4, 6, 6, 8, 9, 9, 10, 10},
            {-1, 2, 3, 4, 6, 7, 8, 9, 9, 10, 10},
            {-1, 2, 4, 4, 6, 7, 8, 9, 10, 10, 10},
            // 31
            {-1, 2, 4, 5, 6, 7, 8, 9, 10, 10, 11},
            {-1, 3, 4, 5, 6, 7, 8, 10, 10, 10, 11},
            {-1, 3, 4, 5, 6, 8, 8, 10, 10, 10, 11},
            {-1, 3, 4, 5, 6, 8, 9, 10, 10, 11, 11},
            {-1, 3, 4, 5, 7, 8, 9, 10, 10, 11, 12},
            {-1, 3, 5, 5, 7, 8, 9, 10, 11, 11, 12},
            {-1, 3, 5, 6, 7, 8, 9, 10, 11, 12, 12},
            {-1, 3, 5, 6, 7, 8, 10, 10, 11, 12, 13},
            {-1, 4, 5, 6, 7, 8, 10, 11, 11, 12, 13},
            {-1, 4, 5, 6, 7, 9, 10, 11, 11, 12, 13},
            // 41
            {-1, 4, 6, 6, 7, 9, 10, 11, 12, 12, 13},
            {-1, 4, 6, 7, 7, 9, 10, 11, 12, 13, 13},
            {-1, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14},
            {-1, 4, 6, 7, 8, 10, 10, 11, 12, 13, 14},
            {-1, 4, 6, 7, 9, 10, 10, 11, 12, 13, 14},
            {-1, 4, 6, 7, 9, 10, 10, 12, 13, 13, 14},
            {-1, 4, 6, 7, 9, 10, 11, 12, 13, 13, 15},
            {-1, 4, 6, 7, 9, 10, 12, 12, 13, 13, 15},
            {-1, 4, 6, 7, 10, 10, 12, 12, 13, 14, 15},
            {-1, 4, 6, 8, 10, 10, 12, 12, 13, 15, 15},
            // 51
            {-1, 5, 7, 8, 10, 10, 12, 12, 13, 15, 15},
            {-1, 5, 7, 8, 10, 11, 12, 12, 13, 15, 15},
            {-1, 5, 7, 9, 10, 11, 12, 12, 14, 15, 15},
            {-1, 5, 7, 9, 10, 11, 12, 13, 14, 15, 16},
            {-1, 5, 7, 10, 10, 11, 12, 13, 14, 16, 16},
            {-1, 5, 8, 10, 10, 11, 12, 13, 15, 16, 16},
            {-1, 5, 8, 10, 11, 11, 12, 13, 15, 16, 17},
            {-1, 5, 8, 10, 11, 12, 12, 13, 15, 16, 17},
            {-1, 5, 9, 10, 11, 12, 12, 14, 15, 16, 17},
            {-1, 5, 9, 10, 11, 12, 13, 14, 15, 16, 18},
            // 61
            {-1, 5, 9, 10, 11, 12, 13, 14, 16, 17, 18},
            {-1, 5, 9, 10, 11, 13, 13, 14, 16, 17, 18},
            {-1, 5, 9, 10, 11, 13, 13, 15, 17, 17, 18},
            {-1, 5, 9, 10, 11, 13, 14, 15, 17, 17, 18},
            {-1, 5, 9, 10, 12, 13, 14, 15, 17, 18, 18},
            {-1, 5, 9, 10, 12, 13, 15, 15, 17, 18, 19},
            {-1, 5, 9, 10, 12, 13, 15, 16, 17, 19, 19},
            {-1, 5, 9, 10, 12, 14, 15, 16, 17, 19, 19},
            {-1, 5, 9, 10, 12, 14, 16, 16, 17, 19, 19},
            {-1, 5, 9, 10, 12, 14, 16, 17, 18, 19, 19},
            // 71
            {-1, 5, 9, 10, 13, 14, 16, 17, 18, 19, 20},
            {-1, 5, 9, 10, 13, 15, 16, 17, 18, 19, 20},
            {-1, 5, 9, 10, 13, 15, 16, 17, 19, 20, 21},
            {-1, 6, 9, 10, 13, 15, 16, 18, 19, 20, 21},
            {-1, 6, 9, 10, 13, 16, 16, 18, 19, 20, 21},
            {-1, 6, 9, 10, 13, 16, 17, 18, 19, 20, 21},
            {-1, 6, 9, 10, 13, 16, 17, 18, 20, 21, 22},
            {-1, 6, 9, 10, 13, 16, 17, 19, 20, 22, 23},
            {-1, 6, 9, 10, 13, 16, 18, 19, 20, 22, 23},
            {-1, 6, 9, 10, 13, 16, 18, 20, 21, 22, 23},
            // 81
            {-1, 6, 9, 10, 13, 17, 18, 20, 21, 22, 23},
            {-1, 6, 9, 10, 14, 17, 18, 20, 21, 22, 24},
            {-1, 6, 9, 11, 14, 17, 18, 20, 21, 23, 24},
            {-1, 6, 9, 11, 14, 17, 19, 20, 21, 23, 24},
            {-1, 6, 9, 11, 14, 17, 19, 21, 22, 23, 24},
            {-1, 7, 10, 11, 14, 17, 19, 21, 22, 23, 25},
            {-1, 7, 10, 12, 14, 17, 19, 21, 22, 24, 25},
            {-1, 7, 10, 12, 14, 18, 19, 21, 22, 24, 25},
            {-1, 7, 10, 12, 15, 18, 19, 21, 22, 24, 26},
            {-1, 7, 10, 12, 15, 18, 19, 21, 23, 25, 26},
            // 91
            {-1, 7, 11, 13, 15, 18, 19, 21, 23, 25, 26},
            {-1, 7, 11, 13, 15, 18, 20, 21, 23, 25, 27},
            {-1, 8, 11, 13, 15, 18, 20, 22, 23, 25, 27},
            {-1, 8, 11, 13, 16, 18, 20, 22, 23, 25, 28},
            {-1, 8, 11, 14, 16, 18, 20, 22, 23, 26, 28},
            {-1, 8, 11, 14, 16, 19, 20, 22, 24, 26, 28},
            {-1, 8, 12, 14, 16, 19, 20, 22, 24, 26, 28},
            {-1, 8, 12, 15, 16, 19, 20, 22, 24, 27, 28},
            {-1, 8, 12, 15, 17, 19, 20, 22, 24, 27, 29},
            {-1, 8, 12, 15, 18, 19, 20, 22, 24, 27, 30},
    };

    private static final int MAX_POWER = POWER_TABLE.length - 1;
}
