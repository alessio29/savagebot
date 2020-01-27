package org.alessio29.savagebot.characters;

import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.initiative.DrawCardResult;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.utils.Utils;

import java.util.*;

public class Character {
    private static final String TOKENS = "tokens";
    private static final String ALL_INIT_CARDS = "allCards";
    private static final String BEST_INIT_CARD = "bestCard";
    private static final String SAWO_INIT_PARAMS = "sawoInitParams";
    private static final String OUT_OF_FIGHT = "outOfFight";

    private static final String HESITANT = "h";

    private String name;
    private Map<String, Object> attributes = new HashMap<>();

    public Character(String name) {
        this.name = name;
    }

    public Character(String name, String params, DrawCardResult cards) {
        this.setAllCards(cards.getCards());
        this.name = name;
        setAttribute(SAWO_INIT_PARAMS, params);
        findBestCard();
    }

    public Character(String charName, String saWoInitParams) {
        this(charName);
        this.setSaWoInitParams(saWoInitParams);
    }

    public String getName() {
        return this.name;
    }


    // ==================== CARDS & INITIATIVE ========================

    private void setSaWoInitParams(String mods) {
        attributes.put(SAWO_INIT_PARAMS, mods);
    }

    public void clearCards() {
        setAttribute(ALL_INIT_CARDS, new ArrayList<Card>());
        setAttribute(BEST_INIT_CARD, null);
    }

    public String getSaWoInitParams() {
        String s = (String) attributes .get(SAWO_INIT_PARAMS);
        return Utils.notNullValue(s);
    }


    public Card getBestCard() {
        Object result = attributes.get(BEST_INIT_CARD);
        return (Card) result;
    }

    public List<Card> getAllCards() {
        List<Card> result = getAttribute(ALL_INIT_CARDS);
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    public void setAllCards(List<Card> allCards) {
        attributes.put(ALL_INIT_CARDS, allCards);
        findBestCard();
    }

    public boolean alreadyDealt() {
        List<Card> cards = getAttribute(ALL_INIT_CARDS);
        return cards != null && !cards.isEmpty();
    }


    private boolean isHesitant() {
        return getSaWoInitParams().trim().contains(HESITANT);
    }

    private void findBestCard() {
        List<Card> allCards = getAllCards();
        if (allCards.isEmpty()) {
            setAttribute(BEST_INIT_CARD, null);
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
        setAttribute(BEST_INIT_CARD, bestCard);
    }

    private void setOutOfFight(boolean b) {
        attributes.put(OUT_OF_FIGHT, b);
    }

    public boolean isOutOfFight() {
        Boolean res = getAttribute(OUT_OF_FIGHT);
        if (res == null) {
            res = true; // this is right!
        }
        return res;
    }

    public void giveCard(DrawCardResult cards) {

        Card bestCard = cards.findBestCard();
        List<Card> allCards = getAllCards();
        allCards.addAll(cards.getCards());
        if (isHesitant() && getBestCard().compareTo(bestCard)<0) {
            setAttribute(BEST_INIT_CARD, bestCard);
        } else {
            findBestCard();
        }
    }

    public void removeFromFight() {
        setOutOfFight(true);
        clearCards();
    }

    public void backToFight() {
        setOutOfFight(false);
    }

    // ==================== TOKENS ========================

    public void addTokens(Integer tokens) {
        Integer oldTokens = getTokens();
        oldTokens = Utils.notNullValue(oldTokens);
        tokens = Utils.notNullValue(tokens);
        tokens = Math.max(oldTokens + tokens, 0);
        setAttribute(TOKENS, tokens);
    }

    public Integer getTokens() {
        return getAttribute(TOKENS);
    }

    public void removeTokens(Integer amount) {
        Integer tokens = getTokens();
        tokens = Utils.notNullValue(tokens);
        if (tokens <= amount) {
            tokens = 0;
        } else {
            tokens -= amount;
        }
        setAttribute(TOKENS, tokens);
    }

    public void removeAllTokens() {
        setAttribute(TOKENS, null);
    }

    // ==================== PRIVATE  ========================

    private <T> T getAttribute(String attribute) {

        if (!attributes.containsKey(attribute)) {
            return null;
        }
        Object result = attributes.get(attribute);

        return (T) result;
    }

    private <T> void setAttribute(String attribute, T value) {
        attributes.put(attribute, value);
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

    public boolean isEmpty() {
        if (attributes.isEmpty()) {
            return true;
        }
        for (Map.Entry<String, Object> e : attributes.entrySet()) {
            if (e.getValue() != null) {
                return false;
            }
        }
        return true;
    }

    public void dealInitiativeCards(Deck deck) {
        DrawCardResult cards = deck.getCardByParams(this.getSaWoInitParams());
        this.setAllCards(cards.getCards());
        this.backToFight();
    }
}