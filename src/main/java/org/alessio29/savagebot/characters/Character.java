package org.alessio29.savagebot.characters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.bennies.BennyType;
import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.initiative.DrawCardResult;
import org.alessio29.savagebot.internal.utils.Utils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Character {
    private static final String HESITANT = "h";

    private String name;
    private String sawoInitParams;
    private Integer tokens;
    private Set<State> states = new HashSet<>();
    private Boolean outOfFight;
    private List<Card> initCards = new ArrayList<>();
    private Card bestCard;
    private Integer bennies;
    private Integer blueBennies;
    private Integer redBennies;
    private Integer whiteBennies;
    private Integer goldenBennies;
    private Boolean onHold;

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
        return Utils.notNullValue(tokens);
    }

    @JsonProperty
    public void setTokens(Integer tokens) {
        this.tokens = tokens;
    }

    @JsonProperty
    public Integer getBennies() {
        return Utils.notNullValue(bennies);
    }

    @JsonProperty
    public void setBennies(int bennies) {
        this.bennies = bennies;
    }

    @JsonProperty
    public String getName() {
        return Utils.notNullValue(this.name);
    }

    @JsonProperty
    public void setName(String newName) {
        this.name = newName;
    }

    @JsonProperty
    public Set getStates() {
        return Utils.notNullValue(states);
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

    @JsonProperty
    private void setOnHold(Boolean onHold) {
        this.onHold = onHold;
    }

    @JsonProperty
    public Boolean isOnHold() {
        if (this.onHold == null) {
            return false;
        }
        return this.onHold;
    }

    public void removeAllBennies() {
        this.bennies = null;
        this.blueBennies = null;
        this.redBennies = null;
        this.whiteBennies = null;
        this.goldenBennies = null;
    }


    @JsonIgnore
    public void giveCard(DrawCardResult cards) {
        List<Card> allCards = getInitCards();
        allCards.addAll(cards.getCards());
        this.initCards = allCards;
        Card bestCard = cards.findBestCard();
        if (isHesitant() && bestCard.compareTo(getBestCard()) > 0) {
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

    public void adjustBennies(int bennies) {
        bennies = Math.max(Utils.notNullValue(getBennies()) + Utils.notNullValue(bennies), 0);
        this.bennies = bennies;
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

        return this.outOfFight == null;
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

    public void addColoredBennies(Map.Entry<BennyColor, Integer> parseBennies) {
        BennyColor type = parseBennies.getKey();
        Integer count = parseBennies.getValue();
        this.adjustBennies(count, type);
    }

    public void takeColoredBennies(Map.Entry<BennyColor, Integer> parseBennies) {
        BennyColor type = parseBennies.getKey();
        Integer count = parseBennies.getValue();
        this.adjustBennies(-count, type);
    }

    private void adjustBennies(Integer count, BennyColor type) {

        switch (type) {
            case BLUE:
                this.blueBennies = Utils.notNullValue(this.blueBennies) + count;
                this.blueBennies = (this.blueBennies<0)?0:this.blueBennies;
                break;
            case RED:
                this.redBennies = Utils.notNullValue(this.redBennies)+ count;
                this.redBennies = (this.redBennies<0)?0:this.redBennies;
                break;
            case WHITE:
                this.whiteBennies = Utils.notNullValue(whiteBennies)+ count;
                this.whiteBennies = (this.whiteBennies <0)?0:this.whiteBennies;
                break;
            case GOLDEN:
                this.goldenBennies = Utils.notNullValue(goldenBennies)+ count;
                this.goldenBennies = (this.goldenBennies<0)?0:this.goldenBennies;
                break;
        }
    }

    public String getBennyValue(BennyType bType) {

        if (bType.equals(BennyType.NORMAL)) {
            return String.valueOf(Utils.notNullValue(this.getBennies()));
        }
        if (bType.equals(BennyType.DEADLANDS)) {
            List<String> result = new ArrayList<>();
            if (Utils.notNullValue(this.goldenBennies)>0) {
                result.add(this.goldenBennies+"G");
            }
            if (Utils.notNullValue(this.whiteBennies)>0) {
                result.add(this.whiteBennies+"W");
            }
            if (Utils.notNullValue(this.redBennies)>0) {
                result.add(this.redBennies+"R");
            }
            if (Utils.notNullValue(this.blueBennies)>0) {
                result.add(this.blueBennies+"B");
            }
            return StringUtils.join(result, ",");
        }
        return "";
    }

    @JsonIgnore
    public void hold() {
        setOnHold(true);
    }

    @JsonIgnore
    public void returnToFight() {
        setOnHold(false);
    }
}