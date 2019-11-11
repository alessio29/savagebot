package org.alessio29.savagebot.cards;

import org.alessio29.savagebot.cards.Deck;
import org.junit.Assert;
import org.junit.Test;

public class TestCards {

	
	@Test
	public void testCardCompare() {

		Assert.assertNotEquals("Jokers must not be equal!", 0, Deck.BLACK_JOKER.compareTo(Deck.COLOR_JOKER));
		Assert.assertEquals("Black Joker must be greater than Colored Joker!", -1, Deck.COLOR_JOKER.compareTo(Deck.BLACK_JOKER));
		Assert.assertEquals("Jokers must be greater than ace!", Deck.SPADES_ACE.compareTo(Deck.BLACK_JOKER), -1);
		Assert.assertEquals("Jokers must be greater than ace!", 1, Deck.COLOR_JOKER.compareTo(Deck.CLUBS_ACE));
		Assert.assertEquals("Spades must be greater than hearts!", 1, Deck.SPADES_JACK.compareTo(Deck.HEARTS_JACK));
		Assert.assertEquals("Hearts must be greater than diamonds!", Deck.DIAMONDS_JACK.compareTo(Deck.HEARTS_JACK), -1);
		Assert.assertEquals("Ace must be greater than King!", 1, Deck.CLUBS_ACE.compareTo(Deck.HEARTS_KING));
	}
}
