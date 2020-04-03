package org.alessio29.savagebot.internal.commands;

import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.Prefixes;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.builders.ResponseBuilder;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.UUID;

public class CommandInterpreter {
    private final CommandRegistry registry = CommandRegistry.getInstance();

    private static Logger log = Logger.getLogger(CommandInterpreter.class);

    public void run(IMessageReceived message, ResponseBuilder responseBuilder) {
        String prefix = Prefixes.getPrefix(message.getAuthorId());

        String rawMessage = message.getRawMessage();
        String strippedMessage = ReplyBuilder.removeBlocks(ReplyBuilder.removeQuotes(rawMessage));
        String[] words = strippedMessage.split("\\s+");

        for (int i = 0; i < words.length; i++) {
            words[i] = StringEscapeUtils.unescapeJava(words[i]);
        }

        int index = 0;
        while (index < words.length) {
            String word = words[index];
            boolean isCommand = false;

            if (word.trim().startsWith(prefix)) {
                String command = word.replaceFirst(prefix, "");

                ICommand cmd = registry.getCommandByName(command);
                if (cmd != null) {
                    isCommand = true;
                    String[] args = Arrays.copyOfRange(words, index + 1, words.length);
                    try {
                        CommandExecutionResult res = cmd.execute(message, args);
                        responseBuilder.addResult(res);
                        index += res.getToSkip();
                    } catch (Exception e) {
                        UUID errorId = UUID.randomUUID();
                        log.debug("Exception Id: "+errorId.toString()+"\nException while executing command: ", e);
                        responseBuilder.reportError(errorId, word, e);
                        index++;
                    }
                } else {
                    for (IParsingCommand pcmd : registry.getRegisteredParsingCommands()) {
                        try {
                            CommandExecutionResult res = pcmd.parseAndExecuteOrNull(message, command);
                            if (res != null) {
                                isCommand = true;
                                index++;
                                responseBuilder.addResult(res);
                                break;
                            }
                        } catch (Exception e) {
                            UUID errorId = UUID.randomUUID();
                            log.debug("Exception while executing command: ", e);
                            responseBuilder.reportError(errorId, word, e);
                            index++;
                        }
                    }
                }
            }

            if (!isCommand) {
                responseBuilder.addRaw(word);
                index++;
            }
        }
    }
}
