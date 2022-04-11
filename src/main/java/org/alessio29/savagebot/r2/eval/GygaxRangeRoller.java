package org.alessio29.savagebot.r2.eval;

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
        public final int modifier;

        public Roll(int dice, int facets, int modifier) {
            this.dice = dice;
            this.facets = facets;
            this.modifier = modifier;
        }

        public Roll(int dice, int facets) {
            this(dice, facets, 0);
        }

        public Roll withModifier(int modifier) {
            return new Roll(dice, facets, modifier);
        }

        public int getMin() {
            return dice;
        }
    }

    private static final Map<Integer, Roll> GYGAX_DICE = new HashMap<>();
    static {
        int[] gygaxFacets = new int[]{4, 6, 8, 10, 12, 20, 100};
        for (int dice = 1; dice < 4; ++dice) {
            for (int facets : gygaxFacets) {
                int delta = dice * facets - dice;
                if (GYGAX_DICE.containsKey(delta))
                    continue;
                GYGAX_DICE.put(delta, new Roll(dice, facets));
            }
        }
    }

    private static final Map<Integer, Roll> NON_GYGAX_DICE = new HashMap<>();
    static {
        int[] nonGygaxFacets = new int[]{3, 5, 7, 14, 16, 24, 30};
        for (int dice = 1; dice < 4; ++dice) {
            for (int facets : nonGygaxFacets) {
                int delta = dice * facets - dice;
                if (NON_GYGAX_DICE.containsKey(delta))
                    continue;
                NON_GYGAX_DICE.put(delta, new Roll(dice, facets));
            }
        }
    }

    public IntResult roll(int min, int max) {
        Roll roll = getRoll(min, max);
        IntResult rollResult = roller.roll(roll.dice, roll.facets);

        int modifiedRollValue = rollResult.getValue() + roll.modifier;

        StringBuilder explained = new StringBuilder();
        explained.append("[").append(roll.dice).append("d").append(roll.facets);
        if (roll.modifier > 0) {
            explained.append("+").append(roll.modifier);
        } else if (roll.modifier < 0) {
            explained.append(roll.modifier);
        }
        explained.append("] ").append(rollResult.getExplained());
        if (roll.modifier > 0) {
            explained.append(" + ").append(roll.modifier);
        } else if (roll.modifier < 0) {
            explained.append(" - ").append(-roll.modifier);
        }

        return new IntResult(modifiedRollValue, explained.toString());
    }

    private Roll getRoll(int min, int max) {
        int delta = max - min;
        Roll gygaxRoll = GYGAX_DICE.get(delta);
        if (gygaxRoll != null) {
            return gygaxRoll.withModifier(min - gygaxRoll.getMin());
        }
        Roll nonGygaxRoll = NON_GYGAX_DICE.get(delta);
        if (nonGygaxRoll != null) {
            return nonGygaxRoll.withModifier(min - nonGygaxRoll.getMin());
        }
        return new Roll(1, max - min + 1, min - 1);
    }
}
