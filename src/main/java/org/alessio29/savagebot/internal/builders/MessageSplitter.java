package org.alessio29.savagebot.internal.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MessageSplitter {
    public static List<String> splitMessage(String message, int partLengthLimit) {
        if (message.length() < partLengthLimit) {
            return Collections.singletonList(message);
        }

        List<String> result = new ArrayList<>();
        StringBuilder partBuilder = new StringBuilder();

        for (String line : message.split(ReplyBuilder.NEWLINE)) {
            line = line.trim();

            if (line.length() > partLengthLimit) {
                if (partBuilder.length() > 0) {
                    result.add(partBuilder.toString().trim());
                    partBuilder.setLength(0);
                }
                result.addAll(splitLine(line, partLengthLimit));
            } else if (partBuilder.length() + line.length() + 1 > partLengthLimit) {
                // Part length here is a bit overestimated,
                // but that should be fine for Discord message length limit.
                result.add(partBuilder.toString().trim());
                partBuilder.setLength(0);
                partBuilder.append(line).append(ReplyBuilder.NEWLINE);
            } else {
                partBuilder.append(line).append(ReplyBuilder.NEWLINE);
            }
        }

        if (partBuilder.length() > 0) {
            result.add(partBuilder.toString().trim());
        }

        return result;
    }

    private static Collection<? extends String> splitLine(String line, int partLengthLimit) {
        if (line.length() < partLengthLimit) {
            return Collections.singletonList(line);
        }

        List<String> result = new ArrayList<>();
        while (line.length() > partLengthLimit) {
            result.add(line.substring(0, partLengthLimit).trim());
            line = line.substring(partLengthLimit);
        }
        result.add(line);

        return result;
    }

}
