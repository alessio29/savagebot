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
		if (value != other.value)
			return false;
		return true;
	}

	public static final Suit CLUBS = new Suit("<:blue_clubs:518183303778533386>", 1);		// трефы
	public static final Suit DIAMONDS = new Suit("<:red_diamonds:518183240746532901>", 2);	// бубны
	public static final Suit HEARTS = new Suit("<:red_hearts:518183058801819648>", 3);		// червы
	public static final Suit SPADES = new Suit("<:blue_spades:518182986231840770>", 4);		// пики
	public static final Suit COLOR = new Suit("<:red_joker:518183609039978575>", 5);		// цветной
	public static final Suit BLACK = new Suit("<:blue_joker:518182956066537505>", 6);		// черный
	
	
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
