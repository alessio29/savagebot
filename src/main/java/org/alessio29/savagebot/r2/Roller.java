package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.internal.Messages;
import org.alessio29.savagebot.r2.tree.GenericRollExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Roller {
    private final Random random;

    public Roller(Random random) {
        this.random = random;
    }

    private int roll(int facetsCount) {
        return random.nextInt(facetsCount) + 1;
    }

    private IntResult roll(int facetsCount, boolean isOpenEnded) {
        int total = 0;
        StringJoiner explained = new StringJoiner("+");

        boolean exploded = false;

        while (true) {
            int die = roll(facetsCount);
            total += die;

            explained.add(Integer.toString(die));

            if (!isOpenEnded || die != facetsCount) {
                break;
            }

            exploded = true;
        }

        String explainedWithTotal;
        if (exploded) {
            explainedWithTotal = explained.toString() + "=" + total;
        } else {
            explainedWithTotal = explained.toString();
        }

        return new IntResult(total, explainedWithTotal);
    }

    private int rollDF() {
        return random.nextInt(3) - 1;
    }

    public IntResult rollAndKeep(
            int dieCount,
            int facetsCount,
            boolean isOpenEnded,
            GenericRollExpression.SuffixOperator suffixOperator,
            int suffixArg
    ) {
        int nDice = dieCount;
        if (suffixOperator == GenericRollExpression.SuffixOperator.ADVANTAGE ||
                suffixOperator == GenericRollExpression.SuffixOperator.DISADVANTAGE) {
            nDice += suffixArg;
        }

        int keptDice = dieCount;
        if (suffixOperator == GenericRollExpression.SuffixOperator.KEEP ||
                suffixOperator == GenericRollExpression.SuffixOperator.KEEP_LEAST) {
            keptDice = suffixArg;
        }

        List<IntResult> dice = IntStream.range(0, nDice)
                .mapToObj(it -> roll(facetsCount, isOpenEnded))
                .collect(Collectors.toList());

        if (keptDice != nDice) {
            dice.sort(IntResult.BY_VALUE);
        }

        int keptStart = 0;
        if (suffixOperator == GenericRollExpression.SuffixOperator.ADVANTAGE ||
                suffixOperator == GenericRollExpression.SuffixOperator.KEEP) {
            keptStart = nDice - keptDice;
        }

        int keptEndExclusive = nDice;
        if (suffixOperator == GenericRollExpression.SuffixOperator.DISADVANTAGE ||
                suffixOperator == GenericRollExpression.SuffixOperator.KEEP_LEAST) {
            keptEndExclusive = keptDice;
        }

        int total = 0;
        StringJoiner explained = new StringJoiner(",", "[", "]");

        for (int i = 0; i < nDice; ++i) {
            IntResult die = dice.get(i);
            if (i >= keptStart && i < keptEndExclusive) {
                total += die.getValue();
                explained.add(die.getExplained());
            } else {
                explained.add(Messages.strikeout(die.getExplained()));
            }
        }

        return new IntResult(total, explained.toString());
    }

    public IntResult rollFudge(int diceCount) {
        int total = 0;

        StringBuilder explained = new StringBuilder();
        explained.append("[");
        for (int i = 0; i < diceCount; ++i) {
            int die = rollDF();
            total += die;
            if (die == -1) {
                explained.append("-");
            } else if (die == 1) {
                explained.append("+");
            } else if (die == 0) {
                explained.append(".");
            }
        }
        explained.append("]");

        return new IntResult(total, explained.toString());
    }

    public IntListResult rollSavageWorlds(int diceCount, int abilityDieFacets, int wildDieFacets) {
        List<IntResult> abilityDice = IntStream.range(0, diceCount)
                .mapToObj(it -> roll(abilityDieFacets, true))
                .sorted(IntResult.BY_VALUE)
                .collect(Collectors.toList());

        IntResult wildDie = roll(wildDieFacets, true);

        StringBuilder explained = new StringBuilder();
        explained.append("[");

        explained.append("wild: ").append(wildDie.getExplained());
        for (IntResult dieResult : abilityDice) {
            explained.append("; ").append(dieResult.getExplained());
        }

        explained.append("]");

        List<IntResult> allDice = new ArrayList<>(abilityDice);
        allDice.add(wildDie);
        allDice.sort(IntResult.BY_VALUE);

        List<Integer> result =
                allDice.subList(1, allDice.size())
                        .stream()
                        .map(IntResult::getValue).collect(Collectors.toList());

        return new IntListResult(result, explained.toString());
    }

    public IntResult rollD66(int digitsCount) {
        StringJoiner result = new StringJoiner(",", "[", "]");
        int total = 0;
        for (int i = 0; i < digitsCount; ++i) {
            int die = roll(6);
            total = total * 10 + die;
            result.add(Integer.toString(die));
        }
        return new IntResult(total, result.toString());
    }

    public IntResult rollSuccessOrFail(
            int diceCount,
            int facetsCount,
            boolean isOpenEnded,
            int successThreshold,
            int failThreshold
    ) {
        List<IntResult> dice = IntStream.range(0, diceCount)
                .mapToObj(it -> roll(facetsCount, isOpenEnded))
                .collect(Collectors.toList());

        int totalSuccesses = 0;
        int totalFailures = 0;
        StringJoiner rolls = new StringJoiner(", ", "[", "]");

        for (IntResult die : dice) {
            int dieValue = die.getValue();
            String dieExplained = die.getExplained();
            if (dieValue >= successThreshold) {
                rolls.add(dieExplained + Messages.SUCCESS_MARK);
                ++totalSuccesses;
            } else if (dieValue <= failThreshold) {
                rolls.add(dieExplained + Messages.FAIL_MARK);
                ++totalFailures;
            } else {
                rolls.add(dieExplained);
            }
        }

        return new IntResult(totalSuccesses - totalFailures, rolls.toString()); }
}
