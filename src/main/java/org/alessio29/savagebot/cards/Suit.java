package org.alessio29.savagebot.cards;

/**
 * 
 * @author aless
 * 
 * This class implements card suits including 'colored' and 'black' for jokers
 * 
 */

public class Suit implements Comparable<Suit>{

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + value;
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
		Suit other = (Suit) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return value == other.value;
	}

	public static final Suit CLUBS = new Suit("\u2663", 1);
	public static final Suit DIAMONDS = new Suit("\u2666", 2);
	public static final Suit HEARTS = new Suit("\u2665", 3);
	public static final Suit SPADES = new Suit("\u2660", 4);
	public static final Suit COLOR = new Suit("\uD83C\uDCCF", 5);
	public static final Suit BLACK = new Suit("\uD83C\uDCBF", 6);

	private String name;
	private int value;
	
	private Suit(String suitName, int value) {
		
		this.name = suitName;
		this.value = value;
		
	}

	public String getName() {
		return name;
	}

	@Override
	public int compareTo(Suit other) {
		
		if (this.value < other.value) {
			return -1;
		}
		if (this.value > other.value) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return name;
	}

}
