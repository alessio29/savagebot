package org.alessio29.savagebot.bennies;

import java.util.LinkedList;
import java.util.List;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Pocket {

	IUser user;
	IGuild guild;
	List<Benny> bennies;
	
	public void put(Benny benny) {
		
		Pocket pocket = Poсkets.getPocket(guild, user);
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

	public Pocket (IUser user2, IGuild guild2) {
		
		this.user = user2;
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
		return " "+white+" белых, "+red+"красных, "+blue+" синих фишек и "+golden+" золотых фишек";
	}
	
}

