package org.alessio29.savagebot.characters;

import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.initiative.DrawCardResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterInitiative implements Comparable<CharacterInitiative> {

    private String name;
    private List<Card> allCards = new ArrayList<>();
    private Card bestCard = Deck.LOWEST_CARD;
    private boolean isHesitant;
	private String params;

	public CharacterInitiative(String name, String params, DrawCardResult cards) {

        this.setAllCards(cards.getCards());
        this.name = name;
        this.isHesitant = params.toLowerCase().contains("h");
        this.params = params;
        findBestCard();
    }

    public CharacterInitiative(String characterName) {
        this.name = characterName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(CharacterInitiative o) {

        return -this.bestCard.compareTo(o.bestCard); // reverse order
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CharacterInitiative other = (CharacterInitiative) obj;
        if (name == null) {
            return other.name == null;
        } else return name.equals(other.name);
    }

    @Override
    public String toString() {
        return "Character [name=" + name + ", card=" + bestCard + "]";
    }

    public List<Card> getAllCards() {
        return allCards;
    }

    private void setAllCards(List<Card> allCards) {
        this.allCards = allCards;
    }

    public Card getBestCard() {
        return bestCard;
    }

	private void findBestCard() {
    	if (isHesitant) {
            Collections.sort(this.allCards, Card::compareTo);
            this.bestCard = this.allCards.get(0);
			if (this.allCards.contains(Deck.BLACK_JOKER)) {
				this.bestCard = Deck.BLACK_JOKER;
			}
			if (this.allCards.contains(Deck.COLOR_JOKER)) {
				this.bestCard = Deck.COLOR_JOKER;
			}
		} else {
			Collections.sort(this.allCards, Card::compareTo);
			this.bestCard = this.allCards.get(this.allCards.size()-1);
		}
    }

	public String getParams() {
    	return this.params;
	}
}
