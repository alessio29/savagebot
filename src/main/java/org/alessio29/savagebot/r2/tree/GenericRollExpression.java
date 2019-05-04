package org.alessio29.savagebot.r2.tree;

import java.util.HashMap;
import java.util.Map;

public class GenericRollExpression extends Expression {
    public enum SuffixOperator {
        KEEP(1, "k", "K"),
        KEEP_LEAST(1, "kl", "KL"),
        ADVANTAGE("adv"),
        DISADVANTAGE("dis"),
        SUCCESS_OR_FAIL("sf");

        private final String image;
        private final String[] aliases;
        private final int requiredArguments;

        SuffixOperator(String image, String... aliases) {
            this.image = image;
            this.aliases = aliases;
            this.requiredArguments = 0;
        }

        SuffixOperator(int requiredArguments, String image, String... aliases) {
            this.image = image;
            this.aliases = aliases;
            this.requiredArguments = requiredArguments;
        }

        public String getImage() {
            return image;
        }

        public String[] getAliases() {
            return aliases;
        }

        public int getRequiredArguments() {
            return requiredArguments;
        }
    }

    private static final Map<String, SuffixOperator> SUFFIX_OPERATORS = new HashMap<>();
    static {
        for (SuffixOperator operator : SuffixOperator.values()) {
            SUFFIX_OPERATORS.put(operator.getImage(), operator);
            for (String alias : operator.aliases) {
                SUFFIX_OPERATORS.put(alias, operator);
            }
        }
    }

    public static SuffixOperator getSuffixOperator(String image) {
        return SUFFIX_OPERATORS.get(image);
    }

    private final Expression diceCountArg;
    private final Expression facetsCountArg;
    private final boolean isOpenEnded;
    private final SuffixOperator suffixOperator;
    private final Expression suffixArg1;
    private final Expression suffixArg2;

    public GenericRollExpression(
            String text,
            Expression diceCountArg,
            Expression facetsCountArg,
            boolean isOpenEnded,
            SuffixOperator suffixOperator,
            Expression suffixArg1,
            Expression suffixArg2
    ) {
        super(text);
        this.diceCountArg = diceCountArg;
        this.facetsCountArg = facetsCountArg;
        this.isOpenEnded = isOpenEnded;
        this.suffixOperator = suffixOperator;
        this.suffixArg1 = suffixArg1;
        this.suffixArg2 = suffixArg2;
    }

    public GenericRollExpression(
            String text,
            Expression diceCountArg,
            Expression facetsCountArg,
            boolean isOpenEnded,
            SuffixOperator suffixOperator,
            Expression suffixArg1
    ) {
        this(text, diceCountArg, facetsCountArg, isOpenEnded, suffixOperator, suffixArg1, null);
    }

    public GenericRollExpression(
            String text,
            Expression diceCountArg,
            Expression facetsCountArg,
            boolean isOpenEnded
    ) {
        this(text, diceCountArg, facetsCountArg, isOpenEnded, null, null);
    }

    public Expression getDiceCountArg() {
        return diceCountArg;
    }

    public Expression getFacetsCountArg() {
        return facetsCountArg;
    }

    public boolean isOpenEnded() {
        return isOpenEnded;
    }

    public SuffixOperator getSuffixOperator() {
        return suffixOperator;
    }

    public Expression getSuffixArg1() {
        return suffixArg1;
    }

    public Expression getSuffixArg2() {
        return suffixArg2;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitGenericRollExpression(this);
    }
}
