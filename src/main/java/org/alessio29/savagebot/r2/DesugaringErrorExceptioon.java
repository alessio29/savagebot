package org.alessio29.savagebot.r2;

class DesugaringErrorExceptioon extends RuntimeException {
    public DesugaringErrorExceptioon(String message) {
        super(message);
    }

    public DesugaringErrorExceptioon(String message, Throwable cause) {
        super(message, cause);
    }
}
