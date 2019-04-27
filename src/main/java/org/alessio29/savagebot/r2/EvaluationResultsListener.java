package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.r2.tree.Expression;

interface EvaluationResultsListener {
    void putExplanation(Expression expression, String explanation);
}
