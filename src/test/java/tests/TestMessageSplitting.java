package tests;

import org.alessio29.savagebot.internal.builders.DiscordResponseBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class TestMessageSplitting {
    @Test
    public void testEmptyMessage() {
        Assert.assertEquals(
                Collections.singletonList(""),
                DiscordResponseBuilder.splitMessage("", 40)
        );
    }

    @Test
    public void testShortMessage() {
        Assert.assertEquals(
                Collections.singletonList("abc"),
                DiscordResponseBuilder.splitMessage("abc", 40)
        );
    }

    @Test
    public void testLongLine() {
        StringBuilder lineBuilder = new StringBuilder();
        for (int i = 0; i < 40; ++i) {
            lineBuilder.append(i).append(" ");
        }

        Assert.assertEquals(
                Arrays.asList(
                        "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16",
                        "17 18 19 20 21 22 23 24 25 26 27 28 29",
                        "30 31 32 33 34 35 36 37 38 39"
                ),
                DiscordResponseBuilder.splitMessage(lineBuilder.toString(), 40)
        );
    }

    @Test
    public void testManyShortLines() {
        StringBuilder lineBuilder = new StringBuilder();
        for (int i = 0; i < 40; ++i) {
            lineBuilder.append(i).append("\n");
        }

        Assert.assertEquals(
                Arrays.asList(
                        "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15",
                        "16\n17\n18\n19\n20\n21\n22\n23\n24\n25\n26\n27\n28",
                        "29\n30\n31\n32\n33\n34\n35\n36\n37\n38\n39"
                ),
                DiscordResponseBuilder.splitMessage(lineBuilder.toString(), 40)
        );
    }

    @Test
    public void testMixOfShortAndLongLines() {
        StringBuilder lineBuilder = new StringBuilder();
        int i = 0;
        for (; i < 10; ++i) {
            lineBuilder.append(i).append("\n");
        }
        for (; i < 25; ++i) {
            lineBuilder.append(i).append(" ");
        }
        for (; i < 35; ++i) {
            lineBuilder.append(i).append("\n");
        }
        for (; i < 40; ++i) {
            lineBuilder.append(i).append(" ");
        }

        Assert.assertEquals(
                Arrays.asList(
                        "0\n1\n2\n3\n4\n5\n6\n7\n8\n9",
                        "10 11 12 13 14 15 16 17 18 19 20 21 22 2",
                        "3 24 25",
                        "26\n27\n28\n29\n30\n31\n32\n33\n34",
                        "35 36 37 38 39"
                ),
                DiscordResponseBuilder.splitMessage(lineBuilder.toString(), 40)
        );
    }
}
