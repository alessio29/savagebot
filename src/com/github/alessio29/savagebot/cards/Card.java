package com.github.alessio29.savagebot.cards;

/**
 * 
 * @author aless
 *
 *	This class implements card
 *	Card defined by suit and rank
 *
 */

public class Card implements Comparable<Card> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
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
		Card other = (Card) obj;
		if (rank == null) {
			if (other.rank != null)
				return false;
		} else if (!rank.equals(other.rank))
			return false;
		if (suit == null) {
			if (other.suit != null)
				return false;
		} else if (!suit.equals(other.suit))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return rank.toString()+" "+suit.toString();
	}

	private Suit suit;
	private Rank rank;
	
	public Card(Suit suit, Rank rank) {
		
		this.suit = suit;
		this.rank = rank;
	}
	
	public Suit getSuit() {
		return suit;
	}

	
	public Rank getRank() {
		return rank;
	}

	@Override
	public int compareTo(Card other) {

		int result = this.getRank().compareTo(other.getRank());
		
		if (result == 0) {
			result = this.getSuit().compareTo(other.getSuit());
		}
		
		return result;
	}
}
