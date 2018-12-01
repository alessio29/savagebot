package org.alessio29.savagebot.cards;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IUser;

public class Hand {

	private IUser user;
	private List<Card> cards = new ArrayList<Card>();
	
	public Hand(IUser user2) {
		this.user = user2;
	}

	public IUser getUser() {
		return user;
	}
	
	public void setUser(IUser user) {
		this.user = user;
	}
	
	public List<Card> getCards() {
		return cards;
	}
	
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public void clear() {
		this.cards.clear();
	}
	
	
}
