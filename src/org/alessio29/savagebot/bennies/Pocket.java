package org.alessio29.savagebot.bennies;

import java.util.LinkedList;
import java.util.List;

import sx.blah.discord.handle.obj.IGuild;

public class Pocket {

	String character;
	IGuild guild;
	List<Benny> bennies;
	
	public void put(Benny benny) {
		
		Pocket pocket = Pockets.getPocket(guild, character);
		pocket.bennies.add(benny);		
	}
	
	public boolean use (BennyColor color) {
		for (Benny b : bennies) {
			if (b.getColor().equals(color)) {
				bennies.remove(b);
				return true;
			}
		}
		return false;
	}

	public Pocket (String characterName, IGuild guild2) {
		
		this.character = characterName;
		this.guild = guild2;
		this.bennies = new LinkedList<Benny>();
	}

	public String getInfo() {

		int blue = 0;
		int red = 0;
		int white = 0;
		int golden = 0;
		for (Benny b : bennies) {
			switch(b.getColor()) {
			case RED : 
				red++;
				break;
			case BLUE : 
				blue++;
				break;
			case WHITE :
				white++;
				break;
			case GOLDEN :
				golden++;
				break;
			}
		}
		return " "+white+" white, "+red+" red, "+blue+" blue and "+golden+" golden benny(ies)";
	}
	
}

