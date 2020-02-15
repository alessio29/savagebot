package org.alessio29.savagebot.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private String user;
    private List<Card> cards = new ArrayList<Card>();

    public Hand(String userId) {
        this.user = userId;
    }

    @JsonProperty
    public String getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(String user) {
        this.user = user;
    }

    @JsonProperty
    public List<Card> getCards() {
        return cards;
    }

    @JsonProperty
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void clear() {
        this.cards.clear();
    }

}
