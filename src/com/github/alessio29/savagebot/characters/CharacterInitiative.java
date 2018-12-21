package com.github.alessio29.savagebot.characters;

import java.util.ArrayList;
import java.util.List;

import com.github.alessio29.savagebot.cards.Card;
import com.github.alessio29.savagebot.cards.Deck;
import com.github.alessio29.savagebot.initiative.DrawCardResult;

public class CharacterInitiative implements Comparable<CharacterInitiative>{

	private String name;
	private List<Card> allCards = new ArrayList<>();
	private Card bestCard = Deck.LOWEST_CARD ; 

	public CharacterInitiative (String name, DrawCardResult cards) {

		this.setBestCard(cards.getBestCard());
		this.setAllCards(cards.getCards());
		this.name = name;
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
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Character [name=" + name + ", card=" + bestCard + "]";
	}

	public List<Card> getAllCards() {
		return allCards;
	}

	public void setAllCards(List<Card> allCards) {
		this.allCards = allCards;
	}

	public Card getBestCard() {
		return bestCard;
	}

	public void setBestCard(Card bestCard) {
		this.bestCard = bestCard;
	}


}
