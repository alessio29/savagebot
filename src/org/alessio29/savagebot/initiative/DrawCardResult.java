package org.alessio29.savagebot.initiative;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alessio29.savagebot.cards.Card;

public class DrawCardResult {

	private List<Card> cards = new ArrayList<>(); 
	private Card bestCard;
	
	public List<Card> getCards() {
		return cards;
	}
	
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
	public Card getBestCard() {
		return bestCard;
	}
	
	public void setBestCard(Card bestCard) {
		this.bestCard = bestCard;
	}

	public DrawCardResult combineWith(DrawCardResult other) {
		
		this.getCards().addAll(other.getCards());
		this.setBestCard(findBestCard());
		return this;
	}

	public Card findBestCard() {
		Collections.sort(this.getCards());
		return this.getCards().get(this.getCards().size()-1);
	}
	
}
