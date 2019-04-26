package org.alessio29.savagebot.internal;

public class CommandExecutionResult {
	
	private String result = "";
	private int toSkip = 0;
	private boolean privateMessage = false;
	
	public CommandExecutionResult(String string, int i) {
		this.result = string;
		this.toSkip = i;
	}

	public CommandExecutionResult() {
	}

	public CommandExecutionResult(String message, int count, boolean privateMessage) {
		this.result = message;
		this.toSkip = count;
		this.setPrivateMessage(privateMessage);
	}

	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}

	public int getToSkip() {
		return toSkip;
	}

	public void setToSkip(int toSkip) {
		this.toSkip = toSkip;
	}

	public boolean isPrivateMessage() {
		return privateMessage;
	}

	public void setPrivateMessage(boolean privateMessage) {
		this.privateMessage = privateMessage;
	}

}
