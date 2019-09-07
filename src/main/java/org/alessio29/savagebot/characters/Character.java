package org.alessio29.savagebot.characters;

public class Character {
    private String name;
    private int tokens;

    public Character (String name, int tokens) {
        this.name = name;
        this.tokens = tokens;
    }

    public String getName() {
        return this.name;
    }

    public int getTokens() {
        return this.tokens;
    }

    public void addTokens(int tokens) {
        this.tokens+=tokens;
    }

    public void removeTokens(int tokens) {
        this.tokens=(this.tokens<=tokens)?0:this.tokens-tokens;
    }

}
