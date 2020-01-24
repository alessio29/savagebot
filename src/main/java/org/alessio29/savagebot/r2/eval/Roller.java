package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.internal.builders.MessageSplitter;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.r2.tree.GenericRollExpression;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Roller {
    private final Random random;

    public Roller(Random random) {
        this.random = random;
    }

    private int roll(int facetsCount) {
        if (facetsCount <= 0) {
            throw new EvaluationErrorException("Facets count should be >0: " + facetsCount);
        }
        return random.nextInt(facetsCount) + 1;
    }

    private IntResult roll(int facetsCount, boolean isOpenEnded) {
        int total = 0;
        StringJoiner explained = new StringJoiner("+");

        while (true) {
            int die = roll(facetsCount);
            total += die;

            explained.add(Integer.toString(die));

            if (!isOpenEnded || die != facetsCount) {
                break;
            }
        }

        return new IntResult(total, Integer.toString(total));
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

        if (facetsCount == 1) {
            return new IntResult(keptDice, Integer.toString(keptDice));
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
                explained.add(ReplyBuilder.strikeout(die.getExplained()));
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
                .sorted(IntResult.BY_VALUE.reversed())
                .collect(Collectors.toList());

        List<IntResult> successes = new ArrayList<>();
        List<IntResult> failures = new ArrayList<>();
        List<IntResult> neutral = new ArrayList<>();
        for (IntResult die : dice) {
            int dieValue = die.getValue();
            if (dieValue >= successThreshold) {
                successes.add(die);
            } else if (dieValue <= failThreshold) {
                failures.add(die);
            } else {
                neutral.add(die);
            }
        }

        StringJoiner explained = new StringJoiner("; ", "[", "]");
        if (!successes.isEmpty()) {
            explained.add("successes(" + successes.size() + "): " + getCommaSeparatedExplanations(successes));
        }
        if (!failures.isEmpty()) {
            explained.add("failures(" + failures.size() + "): " + getCommaSeparatedExplanations(failures));
        }
        if (!neutral.isEmpty()) {
            explained.add("rest: " + getCommaSeparatedExplanations(neutral));
        }



        return new IntResult(successes.size() - failures.size(), explained.toString());
    }

    private String getCommaSeparatedExplanations(List<IntResult> results) {
        return results.stream()
                .map(IntResult::getExplained)
                .collect(Collectors.joining(", "));
    }

    public IntResult rollCarcosa(int diceCount) {
        int d20 = roll(20);
        int die;
        if (d20 <= 4) {
            die = 4;
        } else if (d20 <= 8) {
            die = 6;
        } else if (d20 <= 12) {
            die = 8;
        } else if (d20 <= 16) {
            die = 10;
        } else {
            die = 12;
        }
        return rollAndKeep(diceCount, die, false, null, 0);
    }

    public IntResult rollWegD6(int diceCount) {
        if (diceCount < 1) {
            throw new EvaluationErrorException("Dice count should be at least 1: " + diceCount);
        }

        int wildDieValue = roll(6, true).getValue();

        List<Integer> regularDiceValues = IntStream.range(0, diceCount - 1)
                .mapToObj(it -> roll(6))
                .collect(Collectors.toList());

        int crossedOutValue;
        int total;
        if (wildDieValue == 1) {
            if (diceCount == 1) {
                return new IntResult(0, ReplyBuilder.strikeout("1"));
            }
            crossedOutValue = Collections.max(regularDiceValues);
            total = 0;
        } else {
            crossedOutValue = -1;
            total = wildDieValue;
        }

        StringJoiner regularDiceValuesText = new StringJoiner(" + ");
        if (wildDieValue != 1) {
            regularDiceValuesText.add("w" + String.valueOf(wildDieValue));
        }

        boolean crossedOut = false;
        for (int regularDieValue : regularDiceValues) {
            if (!crossedOut && regularDieValue == crossedOutValue) {
                crossedOut = true;
                regularDiceValuesText.add(ReplyBuilder.strikeout(String.valueOf(regularDieValue)));
            } else {
                total += regularDieValue;
                regularDiceValuesText.add(String.valueOf(regularDieValue));
            }
        }

        if (wildDieValue == 1) {
            return new IntResult(total, "w1; " + regularDiceValuesText.toString());
        } else {
            return new IntResult(total, regularDiceValuesText.toString());
        }
    }
}
