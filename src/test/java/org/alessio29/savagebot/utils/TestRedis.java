package org.alessio29.savagebot.utils;

import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.characters.State;
import org.alessio29.savagebot.initiative.DrawCardResult;
import org.alessio29.savagebot.internal.RedisClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.embedded.RedisServer;

import java.io.IOException;

public class TestRedis {

    private static final String TEST_GUILD = "TEST_GUILD";
    private static final String TEST_CHANNEL = "TEST_CHANNEL";
    private static final String TEST_CHAR_NAME = "TEST_CHAR_1";
    private static final String TEST_CHAR_PARAMS = "q";
    private static RedisServer server;

    @Before
    public void init () throws IOException {
        server = new RedisServer(6379);
        server.start();
    }

    @After
    public void destroy () {
        server.stop();
    }

    @Test
    public void testRedisConnect() {

        RedisClient.setup("localhost", 6379, null);

        Character ch = new Character(TEST_CHAR_NAME, TEST_CHAR_PARAMS);
        ch.addTokens(4);
        ch.addState(State.DISTRACTED);
        ch.addState(State.STUNNED);
        ch.giveCard(new DrawCardResult(Deck.HEARTS_JACK));
        ch.giveCard(new DrawCardResult(Deck.DIAMONDS_KING));
        ch.setSaWoInitParams(TEST_CHAR_PARAMS);
        ch.setOutOfFight(false);

        Characters.storeCharacter(TEST_GUILD, TEST_CHANNEL, ch);
        Characters.save2Redis();
        Characters.getCharacters(TEST_GUILD, TEST_CHANNEL).clear();
        Characters.loadFromRedis();
        Character newChar = Characters.getCharacterByName(TEST_GUILD, TEST_CHANNEL, TEST_CHAR_NAME);

        assert ch.getName().equals(newChar.getName());
        assert ch.getSaWoInitParams().equals(newChar.getSaWoInitParams());
        assert ch.getTokens().equals(newChar.getTokens());
        assert ch.getOutOfFight().equals(newChar.isOutOfFight());
        assert ch.getBestCard().equals(newChar.getBestCard());
        assert ch.getAllCards().equals(newChar.getAllCards());
        assert ch.getStates().equals(newChar.getStates());
        assert ch.getInitCards().equals(newChar.getInitCards());
    }
}
