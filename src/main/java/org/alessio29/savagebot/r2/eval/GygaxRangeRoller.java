package org.alessio29.savagebot.r2.eval;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GygaxRangeRoller {
    private final Roller roller;

    public GygaxRangeRoller(Roller roller) {
        this.roller = roller;
    }

    private static class Roll {
        public final int dice;
        public final int facets;
        public final int multiplier;
        public final int additive;

        public Roll(int dice, int facets, int multiplier, int additive) {
            this.dice = dice;
            this.facets = facets;
            this.multiplier = multiplier;
            this.additive = additive;
        }

        public Roll(int dice, int facets, int modifier) {
            this(dice, facets, 1, modifier);
        }

        public Roll(int dice, int facets) {
            this(dice, facets, 0);
        }

        public Roll withAdditive(int modifier) {
            return new Roll(dice, facets, multiplier, modifier);
        }

        public int getMin() {
            return dice;
        }
    }

    private static void fillRollTable(int[] facets, Map<Integer, Roll> rolls) {
        for (int nDice = 1; nDice < 10; ++nDice) {
            for (int nFacets : facets) {
                int delta = nDice * nFacets - nDice;
                if (rolls.containsKey(delta))
                    continue;
                rolls.put(delta, new Roll(nDice, nFacets));
            }
        }
    }

    private static final int[] GYGAXIAN_FACETS = new int[]{4, 6, 8, 10, 12, 20, 100};
    private static final Map<Integer, Roll> GYGAXIAN_DICE = new HashMap<>();
    static {
        fillRollTable(GYGAXIAN_FACETS, GYGAXIAN_DICE);
    }

    private static final int[] NON_GYGAXIAN_FACETS = new int[]{3, 5, 7, 14, 16, 24, 30};
    private static final Map<Integer, Roll> NON_GYGAXIAN_DICE = new HashMap<>();
    static {
        fillRollTable(NON_GYGAXIAN_FACETS, NON_GYGAXIAN_DICE);
    }

    public IntResult roll(int min, int max) {
        if (min <= 0) {
            throw new EvaluationErrorException("Range min should be > 0: " + min);
        }
        if (max <= 0) {
            throw new EvaluationErrorException("Range max should be > 0: " + max);
        }

        Roll roll = getRoll(min, max);
        IntResult rollResult = roller.roll(roll.dice, roll.facets);

        int modifiedRollValue = rollResult.getValue() * roll.multiplier + roll.additive;

        StringBuilder explained = new StringBuilder();
        explained.append("[").append(roll.dice).append("d").append(roll.facets);
        if (roll.multiplier != 1) {
            explained.append("×").append(roll.multiplier);
        }
        if (roll.additive > 0) {
            explained.append("+").append(roll.additive);
        } else if (roll.additive < 0) {
            explained.append(roll.additive);
        }
        explained.append("] ");
        if (roll.multiplier != 1) {
            explained.append("(");
        }
        explained.append(rollResult.getExplained());
        if (roll.multiplier != 1) {
            explained.append(") × ").append(roll.multiplier);
        }
        if (roll.additive > 0) {
            explained.append(" + ").append(roll.additive);
        } else if (roll.additive < 0) {
            explained.append(" - ").append(-roll.additive);
        }

        return new IntResult(modifiedRollValue, explained.toString());
    }

    private Roll getRoll(int min, int max) {
        int delta = max - min;
        Roll gygaxRoll = GYGAXIAN_DICE.get(delta);
        if (gygaxRoll != null) {
            return gygaxRoll.withAdditive(min - gygaxRoll.getMin());
        }

        if (max % min == 0) {
            Roll roll = tryRollWithMultiplier(min, max, GYGAXIAN_FACETS);
            if (roll != null) return roll;
        }

        Roll nonGygaxRoll = NON_GYGAXIAN_DICE.get(delta);
        if (nonGygaxRoll != null) {
            return nonGygaxRoll.withAdditive(min - nonGygaxRoll.getMin());
        }

        if (max % min == 0) {
            Roll roll = tryRollWithMultiplier(min, max, NON_GYGAXIAN_FACETS);
            if (roll != null) return roll;
        }

        return new Roll(1, max - min + 1, min - 1);
    }

    @Nullable
    private Roll tryRollWithMultiplier(int min, int max, int[] facets) {
        int numFacets = max / min;
        if (Arrays.binarySearch(facets, numFacets) < 0) {
            return null;
        }
        int numDice = min;
        int mult = 1;
        while (numDice % 10 == 0) {
            numDice /= 10;
            mult *= 10;
        }
        return new Roll(numDice, numFacets, mult, 0);
    }
}
