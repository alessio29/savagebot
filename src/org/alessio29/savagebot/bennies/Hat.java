package org.alessio29.savagebot.bennies;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Hat {

	private List<Benny> bennies = new LinkedList<Benny>();
	
	private static final int MAX_WHITE_BENNIES_COUNT = 20;
	private static final int MAX_RED_BENNIES_COUNT = 10;
	private static final int MAX_BLUE_BENNIES_COUNT = 5;

	private static int WHITE_BENNIES_COUNT = 20;
	private static int RED_BENNIES_COUNT = 10;
	private static int BLUE_BENNIES_COUNT = 5;
	private static int GOLDEN_BENNIES_COUNT = 0;

	
	public Hat () {
		
		for (int i=0; i<MAX_WHITE_BENNIES_COUNT; i++) {
			bennies.add(new Benny(BennyColor.WHITE));
		}
		for (int i=0; i<MAX_RED_BENNIES_COUNT; i++) {
			bennies.add(new Benny(BennyColor.RED));
		}		
		for (int i=0; i<MAX_BLUE_BENNIES_COUNT; i++) {
			bennies.add(new Benny(BennyColor.BLUE));
		}
		Collections.shuffle(bennies);
	}


	public Benny getBenny() {
		
		if (bennies.isEmpty()) {
			return null;
		}
		Benny benny = bennies.get(0);
		bennies.remove(benny);
		switch(benny.getColor()) {
		case RED : 
			RED_BENNIES_COUNT--;
			break;
		case BLUE : 
			BLUE_BENNIES_COUNT--;
			break;
		case WHITE :
			WHITE_BENNIES_COUNT--;
			break;
		case GOLDEN :
			GOLDEN_BENNIES_COUNT--;
			break;
		}
		return benny;
	}

	public String getInfo() {
		return "В шляпе "+WHITE_BENNIES_COUNT+" белых фишек, "+
					RED_BENNIES_COUNT+" красных фишек, "+
					BLUE_BENNIES_COUNT+" синих фишек и "+
					GOLDEN_BENNIES_COUNT+" золотых фишек";
	}
	
	
}
