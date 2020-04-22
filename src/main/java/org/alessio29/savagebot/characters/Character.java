package org.alessio29.savagebot.characters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.initiative.DrawCardResult;
import org.alessio29.savagebot.internal.utils.Utils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Character {
    private static final String HESITANT = "h";

    private String name;
    private String sawoInitParams;
    private Integer tokens;
    private Set<State> states = new HashSet<>();
    private Boolean outOfFight;
    private List<Card> initCards = new ArrayList<>();
    private Card bestCard;


    public Character() {
    }

    public Character(String name) {
        this.name = name;
    }

    public Character(String name, String params, DrawCardResult cards) {
        this.setInitCards(cards.getCards());
        this.name = name;
        this.sawoInitParams = params;
        findBestCard();
    }

    public Character(String charName, String saWoInitParams) {
        this(charName);
        if (saWoInitParams != null) {
            this.setSaWoInitParams(saWoInitParams);
        }
    }

    @JsonProperty
    public String getSaWoInitParams() {
        return Utils.notNullValue(sawoInitParams);
    }

    @JsonProperty
    public void setSaWoInitParams(String sawoInitParams) {
        this.sawoInitParams = sawoInitParams;
    }

    @JsonProperty
    public Integer getTokens() {
        return  Utils.notNullValue(tokens);
    }

    @JsonProperty
    public void setTokens(Integer tokens) {
        this.tokens = tokens;
    }

    @JsonProperty
    public String getName() {
        return  Utils.notNullValue(this.name);
    }

    @JsonProperty
    public void setName(String newName) {
        this.name = newName;
    }

    @JsonProperty
    public Set<State> getStates() {
        return  Utils.notNullValue(states);
    }

    @JsonProperty
    public void setStates(Set<State> states) {
        this.states = states;
    }

    @JsonProperty
    public Card getBestCard() {
        return this.bestCard;
    }

    @JsonProperty
    public void setBestCard(Card bestCard) {
        this.bestCard = bestCard;
    }

    @JsonProperty
    public List<Card> getInitCards() {
        return this.initCards;
    }

    @JsonIgnore
    public void setInitCards(List<Card> allCards) {
        this.initCards = allCards;
        findBestCard();
    }

    // ==================== STATES ========================
    @JsonIgnore
    public String getStatesString() {
        return Utils.notNullValue(StringUtils.join(states, ", "));
    }

    @JsonIgnore
    public boolean removeState(State s) {
        return states.remove(s);
    }

    @JsonIgnore
    public boolean addState(State s) {
        if (states == null) {
            states = new HashSet<>();
        }
        return states.add(s);
    }

    @JsonIgnore
    public void clearStates() {
        states.clear();
    }

    // ==================== CARDS & INITIATIVE ========================
    @JsonIgnore
    public void clearCards() {
        this.initCards = new ArrayList<>();
        this.bestCard = null;
    }

    @JsonIgnore
    public boolean alreadyDealt() {
        return initCards != null && !initCards.isEmpty();
    }

    @JsonIgnore
    private boolean isHesitant() {
        return getSaWoInitParams().trim().contains(HESITANT);
    }

    private void findBestCard() {
        List<Card> allCards = getInitCards();
        if (allCards.isEmpty()) {
            this.bestCard = null;
            return;
        }
        Card bestCard;
        if (isHesitant()) {
            allCards.sort(Card::compareTo);
            bestCard = allCards.get(0);
            if (allCards.contains(Deck.BLACK_JOKER)) {
                bestCard = Deck.BLACK_JOKER;
            }
            if (allCards.contains(Deck.COLOR_JOKER)) {
                bestCard = Deck.COLOR_JOKER;
            }
        } else {
            allCards.sort(Card::compareTo);
            bestCard = allCards.get(allCards.size() - 1);
        }
        this.bestCard = bestCard;
    }

    @JsonProperty
    public Boolean isOutOfFight() {

        if (this.outOfFight == null) {
            return true; // this is right!
        }
        return this.outOfFight;
    }

    @JsonProperty
    private void setOutOfFight(Boolean outOfFight) {
        this.outOfFight = outOfFight;
    }

    @JsonIgnore
    public void giveCard(DrawCardResult cards) {
        List<Card> allCards = getInitCards();
        allCards.addAll(cards.getCards());
        this.initCards = allCards;
        Card bestCard = cards.findBestCard();
        if (isHesitant() && bestCard.compareTo(getBestCard()) >0 ){
            this.bestCard = bestCard;
        } else {
            findBestCard();
        }
    }
    @JsonIgnore
    public void removeFromFight() {
        setOutOfFight(true);
        clearCards();
    }

    public void backToFight() {
        setOutOfFight(false);
    }

    // ==================== TOKENS ========================

    public void addTokens(Integer tokens) {
        tokens = Math.max(Utils.notNullValue(getTokens()) + Utils.notNullValue(tokens), 0);
        this.tokens = tokens;
    }

    public void removeTokens(Integer amount) {

        Integer tokens = Utils.notNullValue(getTokens());
        tokens = (tokens <= amount) ? 0 : tokens - amount;
        this.tokens = tokens;
    }

    public void removeAllTokens() {
        this.tokens = null;
    }

    // ==================== PRIVATE  ========================

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

    @JsonIgnore
    public boolean isEmpty() {

        if (this.tokens != null) {
            return false;
        }

        if (this.bestCard != null) {
            return false;
        }

        if (this.initCards != null) {
            return false;
        }

        if (this.sawoInitParams != null) {
            return false;
        }

        if (this.states != null) {
            return false;
        }

        if (this.name != null) {
            return false;
        }

        if (this.outOfFight != null) {
            return false;
        }

        return true;
    }

    public void dealInitiativeCards(Deck deck) {
        DrawCardResult cards = deck.getCardByParams(this.getSaWoInitParams());
        this.setInitCards(cards.getCards());
        this.backToFight();
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", sawoInitParams='" + sawoInitParams + '\'' +
                ", tokens=" + tokens +
                ", states=" + states +
                ", outOfFight=" + outOfFight +
                ", initCards=" + initCards +
                ", bestCard=" + bestCard +
                '}';
    }


}