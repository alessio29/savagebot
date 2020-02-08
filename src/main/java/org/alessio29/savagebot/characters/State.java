package org.alessio29.savagebot.characters;

import java.util.HashMap;

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
    private static final HashMap<String, State> lookupTable = new HashMap<>();
    static {
        lookupTable.put("sha", SHAKEN);
        lookupTable.put("stn", STUNNED);
        lookupTable.put("ent", ENTANGLED);
        lookupTable.put("bnd", BOUND);
        lookupTable.put("dis", DISTRACTED);
        lookupTable.put("vul", VULNERABLE);
    }

    public static State getStateFromString(String stateName) {
        State s = State.valueOfOrNull(stateName.trim().toUpperCase());
        if (s == null ) {
            s = lookupTable.get(stateName.trim().toLowerCase());
        }
        return s;
    }
};
