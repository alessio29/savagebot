package com.github.alessio29.savagebot.cards;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.User;

public class Hand {

	private User user;
	private List<Card> cards = new ArrayList<Card>();
	
	public Hand(User user2) {
		this.user = user2;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
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
