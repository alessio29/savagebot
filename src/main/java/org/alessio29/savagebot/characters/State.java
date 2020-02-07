package org.alessio29.savagebot.characters;

public enum State  {
    SHAKEN,
    STUNNED,
    ENTANGLED,
    BOUND,
    DISTRACTED,
    VULNERABLE;

    public static State valueOfOrNull(String string) {
        try {
            return valueOf(string);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
};
