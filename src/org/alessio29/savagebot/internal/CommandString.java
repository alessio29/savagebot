package org.alessio29.savagebot.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandString {

	private List<String> commands = new ArrayList<>();
	private int currentWord = 0;
	
	
	public CommandString(String content) {

		setCommands(Arrays.asList(content.split(" ")));
	}


	public int getCurrentWord() {
		return currentWord;
	}


	public void setCurrentWord(int currentWord) {
		this.currentWord = currentWord;
	}


	public List<String> getCommands() {
		return commands;
	}


	public void setCommands(List<String> commands) {
		this.commands = commands;
	}
	
	

}
