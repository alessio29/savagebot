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

//	public static final Suit CLUBS = new Suit("<:blue_clubs:518746638811136000>", 1);		
//	public static final Suit DIAMONDS = new Suit("<:red_diamonds:518746602526081044>", 2);	 
//	public static final Suit HEARTS = new Suit("<:red_hearts:518746570666147860>", 3);		 
//	public static final Suit SPADES = new Suit("<:blue_spades:518745722338672640>", 4);		 
//	public static final Suit COLOR = new Suit("<:red_joker:518745763241656320>", 5);		
//	public static final Suit BLACK = new Suit("<:blue_joker:518745696317472802>", 6);		

	public static final Suit CLUBS = new Suit(":clubs:", 1);
	public static final Suit DIAMONDS = new Suit(":diamonds:", 2);	
	public static final Suit HEARTS = new Suit(":hearts:", 3);
	public static final Suit SPADES = new Suit(":spades:", 4);
	public static final Suit COLOR = new Suit(":black_joker:", 5);		
	public static final Suit BLACK = new Suit("<:black_joker_card:518750947753590820>", 6);		

	
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
