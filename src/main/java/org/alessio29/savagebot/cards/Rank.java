package org.alessio29.savagebot.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author aless
 * 
 * This class implements ranks or cards including joker
 *
 */

public class Rank implements Comparable<Rank>{
	
	public static final String[] names = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "Joker" };
	
	public static final Rank TWO 	= new Rank(2);
	public static final Rank THREE	= new Rank(3);
	public static final Rank FOUR 	= new Rank(4);
	public static final Rank FIVE 	= new Rank(5);
	public static final Rank SIX	= new Rank(6);
	public static final Rank SEVEN 	= new Rank(7);
	public static final Rank EIGHT 	= new Rank(8);
	public static final Rank NINE 	= new Rank(9);
	public static final Rank TEN 	= new Rank(10);
	public static final Rank JACK 	= new Rank(11);
	public static final Rank QUEEN 	= new Rank(12);
	public static final Rank KING 	= new Rank(13);
	public static final Rank ACE	= new Rank(14);
	public static final Rank JOKER 	= new Rank(15);
	
	private int rank;

	private Rank(int rank) {
		if (rank<2 || rank>15) {
			throw new IllegalArgumentException();
		}
		this.rank=rank;
	}

	@Override
	public String toString() {
		return names[rank-2];
	}

	public Rank() {
	}

	@JsonProperty
	public void setRank(int newRank) {
		rank = newRank;
	}

	@JsonProperty
	public int getRank() {
		return rank;
	}

	@Override
	public int compareTo(Rank other) {
		
		if (this.rank < other.rank) {
			return -1;
		}
		if (this.rank > other.rank) {
			return 1;
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rank;
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
		Rank other = (Rank) obj;
		return rank == other.rank;
	}

}
