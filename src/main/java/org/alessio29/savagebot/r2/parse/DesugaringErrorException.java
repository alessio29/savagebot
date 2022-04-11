package org.alessio29.savagebot.r2.parse;

class DesugaringErrorException extends RuntimeException {
    public DesugaringErrorException(String message) {
        super(message);
    }

    public DesugaringErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
