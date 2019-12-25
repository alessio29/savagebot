package org.alessio29.savagebot.characters;

import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.initiative.DrawCardResult;

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
        attributes.put(SAWO_INIT_PARAMS, params);
        findBestCard();
    }

    public Character(String charName, String mods) {
        this(charName);
        this.setParams(mods);
    }

    private void setParams(String mods) {
        attributes.put(SAWO_INIT_PARAMS, mods);
    }

    public void clearCards() {
        attributes.put(ALL_INIT_CARDS, new ArrayList<Card>());
        attributes.put(BEST_INIT_CARD, null);
    }

    public String getParams() {
        String s = (String) attributes .get(SAWO_INIT_PARAMS);
        return (s == null) ? "" : s;
    }


    public Card getBestCard() {
        Object result = attributes.get(BEST_INIT_CARD);
        return (Card) result;
    }

    public List<Card> getAllCards() {
        Object result = attributes.computeIfAbsent(ALL_INIT_CARDS, k -> new ArrayList<Card>());
        return (List<Card>) result;
    }

    public void setAllCards(List<Card> allCards) {
        attributes.put(ALL_INIT_CARDS, allCards);
        findBestCard();
    }

    public String getName() {
        return this.name;
    }


    public void addTokens(Integer tokens) {
        Integer oldTokens = 0;
        if (attributes.containsKey(TOKENS)) {
            oldTokens = (Integer) attributes.get(TOKENS);
        }
        attributes.put(TOKENS, tokens + oldTokens);
    }

    public Integer getTokens() {
        return getAttribute(TOKENS, Integer.class);
    }

    public void removeTokens(Integer amount) {
        Integer tokens = getTokens();
        tokens = (tokens == null) ? 0 : tokens;
        if (tokens <= amount) {
            tokens = 0;
        } else {
            tokens -= amount;
        }
        setAttribute(TOKENS, tokens);
    }

    public boolean alreadyDealt() {
        List<Card> cards = (List<Card>) attributes.get(ALL_INIT_CARDS);
        return cards != null && !cards.isEmpty();
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

    private boolean isHesitant() {
        return paramContains(HESITANT);
    }

    private boolean paramContains(String str) {
        String params = (String) attributes.get(SAWO_INIT_PARAMS);
        if (params == null || params.isEmpty()) {
            return false;
        }
        return params.trim().contains(str);
    }

    private void findBestCard() {
        List<Card> allCards = getAllCards();
        if (allCards.isEmpty()) {
            attributes.put(BEST_INIT_CARD, null);
            return;
        }
        Card bestCard;
        if (isHesitant()) {
            Collections.sort(allCards, Card::compareTo);
            bestCard = allCards.get(0);
            if (allCards.contains(Deck.BLACK_JOKER)) {
                bestCard = Deck.BLACK_JOKER;
            }
            if (allCards.contains(Deck.COLOR_JOKER)) {
                bestCard = Deck.COLOR_JOKER;
            }
        } else {
            Collections.sort(allCards, Card::compareTo);
            bestCard = allCards.get(allCards.size() - 1);
        }
        attributes.put(BEST_INIT_CARD, bestCard);
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

    private void setOutOfFight(boolean b) {
        attributes.put(OUT_OF_FIGHT, b);
    }

    public boolean isOutOfFight() {
        Boolean res = (Boolean) attributes.get(OUT_OF_FIGHT);
        return (res == null )? false : res ;
    }

    public void giveCard(DrawCardResult cards) {

        Card bestCard = cards.findBestCard();
        List<Card> allCards = getAllCards();
        allCards.addAll(cards.getCards());
        if (isHesitant() && getBestCard().compareTo(bestCard)<0) {
            attributes.put(BEST_INIT_CARD, bestCard);
        } else {
            findBestCard();
        }
    }

    public void removeFromFight() {
        setOutOfFight(true);
        clearCards();
    }

    public void resetInitiative() {
        clearCards();
        setOutOfFight(false);
    }

    public void backToFight() {
        setOutOfFight(false);
    }
}