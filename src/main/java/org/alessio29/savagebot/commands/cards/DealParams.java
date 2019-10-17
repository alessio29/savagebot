package org.alessio29.savagebot.commands.cards;

class DealParams {

	private String characterName;
	private String params;
	
	DealParams(String c, String params) {
		
		this.characterName = c;
		this.setParams(params);
	}
	
	String getParams() {
		return params;
	}

	private void setParams(String params) {
		this.params = params;
	}

	String getCharacterName() {
		return characterName;
	}

}