package org.alessio29.savagebot.r2.eval;

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
        int openEndedRepeats = 0;
        int openEndedLast;
        StringJoiner explained = new StringJoiner("+");

        while (true) {
            int die = roll(facetsCount);
            openEndedLast = die;
            total += die;

            explained.add(Integer.toString(die));

            if (!isOpenEnded || die != facetsCount) {
                break;
            }

            ++openEndedRepeats;
        }

        String explainedWithTotal;
        if (openEndedRepeats > 1) {
            explainedWithTotal = openEndedRepeats + "x" + facetsCount + "+" + openEndedLast + "=" + total;
        } else {
            explainedWithTotal = Integer.toString(total);
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
        StringJoiner explained = new StringJoiner(" + ", "", "");

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
                explained.append("0");
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

        for (IntResult dieResult : abilityDice) {
            explained.append(dieResult.getExplained()).append("; ");
        }
        explained.append("w").append(wildDie.getExplained());

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
        int total = 0;
        for (int i = 0; i < digitsCount; ++i) {
            int die = roll(6);
            total = total * 10 + die;
        }
        return new IntResult(total, Integer.toString(total));
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
