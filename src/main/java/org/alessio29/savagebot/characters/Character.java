package org.alessio29.savagebot.characters;

import java.util.HashMap;
import java.util.Map;

public class Character {
    private static final String TOKENS = "tokens";

    private String name;
    private Map<String, Object> attributes = new HashMap<>();


    public Character(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Character character = (Character) o;

        return name.equals(character.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void addTokens(int tokens) {

        Integer oldTokens = 0;
        if (attributes.containsKey(TOKENS)) {
            oldTokens = (Integer) attributes.get(TOKENS);
        }
        attributes.put(TOKENS, tokens + oldTokens);
    }

    private <T> T getAttribute(String attribute, Class<T> clazz) {
        if (!attributes.containsKey(attribute)) {
            return null;
        }
        Object result = attributes.get(attribute);
        return (T) result;
    }

    private <T> void setAttribute(String attribute, T value) {
        attributes.put(attribute, value);
    }

    public Integer getTokens() {
        return getAttribute(TOKENS, Integer.class);
    }

    public void removeTokens(int amount) {

        Integer tokens = getTokens();
        tokens = (tokens == null) ? 0 : tokens;

        if (tokens <= amount) {
            tokens = 0;
        } else {
            tokens -= amount;
        }
        setAttribute(TOKENS, tokens);
    }
}
