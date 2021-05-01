package org.alessio29.savagebot.r2.eval;

class EvaluationErrorException extends RuntimeException {
    EvaluationErrorException(String s) {
        super(s);
    }

    EvaluationErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
