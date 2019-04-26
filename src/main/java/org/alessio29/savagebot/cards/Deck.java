package org.alessio29.savagebot.cards;

import org.alessio29.savagebot.initiative.DrawCardResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * 
 * @author aless
 * 
 * This class implements deck of cards
 * All suits and ranks defined here   
 *
 */
public class Deck {

	public static final String IMPROVED_LEVELHEADED = "il";
	public static final String LEVELHEADED = "l";
	public static final String QUICK = "q";
	
	
	private ArrayList<Card> currentDeck;
	private boolean shuffleNeeded = false;
	private boolean jokerDealt = false;
	
	@SuppressWarnings("unchecked")
	private Deck(ArrayList<Card> initialdeck) {
		this.currentDeck = (ArrayList<Card>) initialdeck.clone();
	}

	public static Deck createNewDeck() {
		
		Deck result = new Deck(INITIAL_DECK); 
		result.shuffle();
		return result; 
	}

	@SuppressWarnings("unchecked")
	public void shuffle() {
		currentDeck = (ArrayList<Card>) INITIAL_DECK.clone();
		Collections.shuffle(currentDeck);
		setShuffleNeeded(false);
		setJokerDealt(false);
		
	}

	public DrawCardResult getCardByParams(String params) {
	
		DrawCardResult result = null;
		Card limit = Deck.LOWEST_CARD; 

		params = params.trim();
		
		int count = 1;

		if (params.contains(IMPROVED_LEVELHEADED)) {
			count = 3;
			params = params.replace(IMPROVED_LEVELHEADED, "");
		} else if (params.contains(LEVELHEADED)) {
			count = 2;
			params = params.replace(LEVELHEADED, "");
		}
		
		if (params.contains(QUICK)) {
			limit = Deck.LOWEST_QUICK_CARD;
			params = params.replace(QUICK, "");
		}
		
		result = getCard(limit);
		
		for (int i = 1; i<count; i++ ) {
			result = result.combineWith(getCard(limit)); 
		}
		return result;
	}
	
	public DrawCardResult getCard(Card limit) {
		
		if (limit==null) {
			limit = Deck.LOWEST_CARD;
		}
		
		DrawCardResult result = new DrawCardResult();
		
		Card newCard = getCard();
		result.getCards().add(newCard);
		while (newCard!=null && newCard.compareTo(limit)==-1) {
			newCard = getCard();
			result.getCards().add(newCard);
		}
		result.setBestCard(result.findBestCard());
		return result;
	}
	
	public Card getCard() {
		
		if (currentDeck.size()>0) {
			int last = currentDeck.size()-1;
			Card c = currentDeck.get(last);
			currentDeck.remove(last);
			if (c.equals(BLACK_JOKER) || c.equals(COLOR_JOKER)) {
				setJokerDealt(true);
			}
			return c;
		}
		return null;
	}

	public boolean isShuffleNeeded() {
		return shuffleNeeded;
	}

	public void setShuffleNeeded(boolean shuffleNeeded) {
		this.shuffleNeeded = shuffleNeeded;
	}

	@Override
	public String toString() {
		return "Deck [currentDeck=" + currentDeck + ", shuffleNeeded=" + shuffleNeeded + "]";
	}
	
	
	public boolean isJokerDealt() {
		return jokerDealt;
	}

	public void setJokerDealt(boolean jokerDealt) {
		this.jokerDealt = jokerDealt;
	}

	public boolean isEmpty() {
		return currentDeck.isEmpty();
	}

	public static final Card SPADES_TWO = new Card(Suit.SPADES, Rank.TWO);
	public static final Card SPADES_THREE = new Card(Suit.SPADES, Rank.THREE);
	public static final Card SPADES_FOUR = new Card(Suit.SPADES, Rank.FOUR);
	public static final Card SPADES_FIVE = new Card(Suit.SPADES, Rank.FIVE);
	public static final Card SPADES_SIX = new Card(Suit.SPADES, Rank.SIX);
	public static final Card SPADES_SEVEN = new Card(Suit.SPADES, Rank.SEVEN);
	public static final Card SPADES_EIGHT = new Card(Suit.SPADES, Rank.EIGHT);
	public static final Card SPADES_NINE = new Card(Suit.SPADES, Rank.NINE);
	public static final Card SPADES_TEN = new Card(Suit.SPADES, Rank.TEN);
	public static final Card SPADES_JACK = new Card(Suit.SPADES, Rank.JACK);
	public static final Card SPADES_QUEEN = new Card(Suit.SPADES, Rank.QUEEN);
	public static final Card SPADES_KING = new Card(Suit.SPADES, Rank.KING);
	public static final Card SPADES_ACE = new Card(Suit.SPADES, Rank.ACE);

	public static final Card HEARTS_TWO = new Card(Suit.HEARTS, Rank.TWO);
	public static final Card HEARTS_THREE = new Card(Suit.HEARTS, Rank.THREE);
	public static final Card HEARTS_FOUR = new Card(Suit.HEARTS, Rank.FOUR);
	public static final Card HEARTS_FIVE = new Card(Suit.HEARTS, Rank.FIVE);
	public static final Card HEARTS_SIX = new Card(Suit.HEARTS, Rank.SIX);
	public static final Card HEARTS_SEVEN = new Card(Suit.HEARTS, Rank.SEVEN);
	public static final Card HEARTS_EIGHT = new Card(Suit.HEARTS, Rank.EIGHT);
	public static final Card HEARTS_NINE = new Card(Suit.HEARTS, Rank.NINE);
	public static final Card HEARTS_TEN = new Card(Suit.HEARTS, Rank.TEN);
	public static final Card HEARTS_JACK = new Card(Suit.HEARTS, Rank.JACK);
	public static final Card HEARTS_QUEEN = new Card(Suit.HEARTS, Rank.QUEEN);
	public static final Card HEARTS_KING = new Card(Suit.HEARTS, Rank.KING);
	public static final Card HEARTS_ACE = new Card(Suit.HEARTS, Rank.ACE);

	public static final Card DIAMONDS_TWO = new Card(Suit.DIAMONDS, Rank.TWO);
	public static final Card DIAMONDS_THREE = new Card(Suit.DIAMONDS, Rank.THREE);
	public static final Card DIAMONDS_FOUR = new Card(Suit.DIAMONDS, Rank.FOUR);
	public static final Card DIAMONDS_FIVE = new Card(Suit.DIAMONDS, Rank.FIVE);
	public static final Card DIAMONDS_SIX = new Card(Suit.DIAMONDS, Rank.SIX);
	public static final Card DIAMONDS_SEVEN = new Card(Suit.DIAMONDS, Rank.SEVEN);
	public static final Card DIAMONDS_EIGHT = new Card(Suit.DIAMONDS, Rank.EIGHT);
	public static final Card DIAMONDS_NINE = new Card(Suit.DIAMONDS, Rank.NINE);
	public static final Card DIAMONDS_TEN = new Card(Suit.DIAMONDS, Rank.TEN);
	public static final Card DIAMONDS_JACK = new Card(Suit.DIAMONDS, Rank.JACK);
	public static final Card DIAMONDS_QUEEN = new Card(Suit.DIAMONDS, Rank.QUEEN);
	public static final Card DIAMONDS_KING = new Card(Suit.DIAMONDS, Rank.KING);
	public static final Card DIAMONDS_ACE = new Card(Suit.DIAMONDS, Rank.ACE);

	public static final Card CLUBS_TWO = new Card(Suit.CLUBS, Rank.TWO);
	public static final Card CLUBS_THREE = new Card(Suit.CLUBS, Rank.THREE);
	public static final Card CLUBS_FOUR = new Card(Suit.CLUBS, Rank.FOUR);
	public static final Card CLUBS_FIVE = new Card(Suit.CLUBS, Rank.FIVE);
	public static final Card CLUBS_SIX = new Card(Suit.CLUBS, Rank.SIX);
	public static final Card CLUBS_SEVEN = new Card(Suit.CLUBS, Rank.SEVEN);
	public static final Card CLUBS_EIGHT = new Card(Suit.CLUBS, Rank.EIGHT);
	public static final Card CLUBS_NINE = new Card(Suit.CLUBS, Rank.NINE);
	public static final Card CLUBS_TEN = new Card(Suit.CLUBS, Rank.TEN);
	public static final Card CLUBS_JACK = new Card(Suit.CLUBS, Rank.JACK);
	public static final Card CLUBS_QUEEN = new Card(Suit.CLUBS, Rank.QUEEN);
	public static final Card CLUBS_KING = new Card(Suit.CLUBS, Rank.KING);
	public static final Card CLUBS_ACE = new Card(Suit.CLUBS, Rank.ACE);

	public static final Card BLACK_JOKER = new Card(Suit.BLACK, Rank.JOKER);
	public static final Card COLOR_JOKER = new Card(Suit.COLOR, Rank.JOKER);
	
	private static final ArrayList<Card> INITIAL_DECK = new ArrayList<Card>(
			Arrays.asList(SPADES_TWO, SPADES_THREE, SPADES_FOUR, SPADES_FIVE, 
			SPADES_SIX, SPADES_SEVEN, SPADES_EIGHT, SPADES_NINE, SPADES_TEN, SPADES_JACK, SPADES_QUEEN, SPADES_KING, SPADES_ACE, 
			
			HEARTS_TWO, HEARTS_THREE, HEARTS_FOUR, HEARTS_FIVE, 
			HEARTS_SIX, HEARTS_SEVEN, HEARTS_EIGHT, HEARTS_NINE, HEARTS_TEN, HEARTS_JACK, HEARTS_QUEEN, HEARTS_KING, HEARTS_ACE, 
			
			DIAMONDS_TWO, DIAMONDS_THREE, DIAMONDS_FOUR, DIAMONDS_FIVE, 
			DIAMONDS_SIX, DIAMONDS_SEVEN, DIAMONDS_EIGHT, DIAMONDS_NINE, DIAMONDS_TEN, DIAMONDS_JACK, DIAMONDS_QUEEN, DIAMONDS_KING, DIAMONDS_ACE, 
			
			CLUBS_TWO, CLUBS_THREE, CLUBS_FOUR, CLUBS_FIVE, 
			CLUBS_SIX, CLUBS_SEVEN, CLUBS_EIGHT, CLUBS_NINE, CLUBS_TEN, CLUBS_JACK, CLUBS_QUEEN, CLUBS_KING, CLUBS_ACE, 
			
			COLOR_JOKER, BLACK_JOKER 
			));

	public static final Card LOWEST_CARD = CLUBS_TWO;
	private static final Card LOWEST_QUICK_CARD = CLUBS_SIX;
	
}