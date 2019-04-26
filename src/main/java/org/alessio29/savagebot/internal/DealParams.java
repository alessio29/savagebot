package org.alessio29.savagebot.internal;

public class DealParams {

	private String characterName;
	private String params;
	
	public DealParams(String c, String params) {
		
		this.characterName = c;
		this.setParams(params);
	}
	
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String name) {
		this.characterName = name;
	}

}