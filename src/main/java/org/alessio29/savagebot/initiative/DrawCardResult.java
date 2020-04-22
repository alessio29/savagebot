package org.alessio29.savagebot.initiative;

import org.alessio29.savagebot.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrawCardResult {

	private List<Card> cards = new ArrayList<>(); 
	private Card bestCard;

	public DrawCardResult() {
	}

    public DrawCardResult(Card c1) {
		this.cards.add(c1);
    	this.bestCard = c1;
    }

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

	public DrawCardResult combineWith(DrawCardResult other, boolean normalSortingOrder) {
		
		this.getCards().addAll(other.getCards());
		if (normalSortingOrder) {
			this.setBestCard(findBestCard());
		} else {
			this.setBestCard(findWorstCard());
		}
		return this;
	}

	public Card findBestCard() {
		Collections.sort(this.getCards());
		if (this.getCards().size() > 1) {
			return this.getCards().get(this.getCards().size()-1);
		}
		return this.getCards().get(0);

	}

	public Card findWorstCard() {
		Collections.sort(this.getCards());
		return this.getCards().get(0);
	}
}
