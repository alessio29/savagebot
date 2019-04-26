package com.github.alessio29.savagebot.tests;

import org.junit.Assert;
import org.junit.Test;

import com.github.alessio29.savagebot.cards.Deck;

public class TestCards {

	
	@Test
	public void testCardCompare() {
		
		Assert.assertFalse("Jokers must not be equal!", Deck.BLACK_JOKER.compareTo(Deck.COLOR_JOKER)==0);
		Assert.assertTrue("Black Joker must be greater then Colored!", Deck.COLOR_JOKER.compareTo(Deck.BLACK_JOKER)==-1);
		Assert.assertTrue("Jokers must be greater than ace!", Deck.SPADES_ACE.compareTo(Deck.BLACK_JOKER)==-1);
		Assert.assertTrue("Jokers must be greater than ace!", Deck.COLOR_JOKER.compareTo(Deck.CLUBS_ACE)==1);
		Assert.assertTrue("Spades must be greater than hearts!", Deck.SPADES_JACK.compareTo(Deck.HEARTS_JACK)==1);
		Assert.assertFalse("Hearts must be greater than diamonds!", Deck.DIAMONDS_JACK.compareTo(Deck.HEARTS_JACK)!=-1);
		Assert.assertTrue("Ace must be greater than King!", Deck.CLUBS_ACE.compareTo(Deck.HEARTS_KING)==1);
	}
}
