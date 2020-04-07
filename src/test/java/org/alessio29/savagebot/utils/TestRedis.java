package org.alessio29.savagebot.utils;

import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.characters.State;
import org.alessio29.savagebot.initiative.DrawCardResult;
import org.alessio29.savagebot.internal.RedisClient;
import org.junit.*;
import org.jetbrains.annotations.NotNull;

import redis.embedded.RedisServer;

import java.io.IOException;

public class TestRedis {

    private static final String TEST_GUILD = "TEST_GUILD";
    private static final String TEST_CHANNEL = "TEST_CHANNEL";
    private static final String TEST_CHAR_NAME = "TEST_CHAR_1";
    private static final String TEST_CHAR_PARAMS = "q";
    private static RedisServer server;

    @BeforeClass
    public static void init () throws IOException {
        server = new RedisServer(6379);
        server.start();
    }

    @AfterClass
    public static void destroy () {
        server.stop();
    }

    @Test
    public void testRedisConnect() {

        RedisClient.setup("localhost", 6379, null);

        Character ch = getCharacter();

        Characters.storeCharacter(TEST_GUILD, TEST_CHANNEL, ch);
        Characters.getCharacters(TEST_GUILD, TEST_CHANNEL).clear();
        Characters.loadFromRedis();
        Character newChar = Characters.getCharacterByName(TEST_GUILD, TEST_CHANNEL, TEST_CHAR_NAME);

        assert ch.getName().equals(newChar.getName());
        assert ch.getSaWoInitParams().equals(newChar.getSaWoInitParams());
        assert ch.getTokens().equals(newChar.getTokens());
        assert ch.isOutOfFight().equals(newChar.isOutOfFight());
        assert ch.getAllCards().equals(newChar.getAllCards());
        assert ch.getStates().equals(newChar.getStates());
        assert ch.getInitCards().equals(newChar.getInitCards());
    }

    @NotNull
    private Character getCharacter() {
        Character ch = new Character(TEST_CHAR_NAME, TEST_CHAR_PARAMS);
        ch.addTokens(4);
        ch.addState(State.DISTRACTED);
        ch.addState(State.STUNNED);
        ch.giveCard(new DrawCardResult(Deck.HEARTS_JACK));
        ch.giveCard(new DrawCardResult(Deck.DIAMONDS_KING));
        ch.setSaWoInitParams(TEST_CHAR_PARAMS);
        ch.removeFromFight();
        return ch;
    }

    @Test
    public void testRedisDelete() {
        Character ch = getCharacter();
        Characters.storeCharacter(TEST_GUILD, TEST_CHANNEL, ch);
        Characters.getCharacters(TEST_GUILD, TEST_CHANNEL).clear();
        Characters.loadFromRedis();
        Character newChar1 = Characters.getCharacterByName(TEST_GUILD, TEST_CHANNEL, TEST_CHAR_NAME);
        assert newChar1!=null;
        Characters.removeCharacter(TEST_GUILD, TEST_CHANNEL, TEST_CHAR_NAME);
        Characters.getCharacters(TEST_GUILD, TEST_CHANNEL).clear();
        Characters.loadFromRedis();
        Character newChar2 = Characters.getCharacterByName(TEST_GUILD, TEST_CHANNEL, TEST_CHAR_NAME);
        assert newChar2==null;
    }

}
