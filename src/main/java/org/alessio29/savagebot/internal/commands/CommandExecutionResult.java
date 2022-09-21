package org.alessio29.savagebot.internal.commands;

public class CommandExecutionResult {
	
	private String result = "";
	private int toSkip = 0;
	private boolean privateMessage = false;
	
	public CommandExecutionResult(String string, int i) {
		this.result = string;
		this.toSkip = i;
	}

	public CommandExecutionResult(String string) {
		this.result = string;
	}

	public CommandExecutionResult(String string, boolean privateMessage) {
		this.result = string;
		this.privateMessage = privateMessage;
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

	int getToSkip() {
		return toSkip;
	}

	public boolean isPrivateMessage() {
		return privateMessage;
	}

	private void setPrivateMessage(boolean privateMessage) {
		this.privateMessage = privateMessage;
	}

	@Override
	public String toString() {
		return "CommandExecutionResult{" +
				"result='" + result + '\'' +
				", toSkip=" + toSkip +
				", privateMessage=" + privateMessage +
				'}';
	}
}
