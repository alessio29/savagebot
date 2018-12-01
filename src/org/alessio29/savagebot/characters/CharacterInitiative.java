package org.alessio29.savagebot.characters;

import org.alessio29.savagebot.cards.Card;

public class CharacterInitiative implements Comparable<CharacterInitiative>{

	private String name;
	private Card card; 

	public CharacterInitiative (String name, Card c) {

		this.card = c;
		this.name = name;

	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(CharacterInitiative o) {

		return -this.card.compareTo(o.card);
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
		return "Character [name=" + name + ", card=" + card + "]";
	}


}
