package com.logicalkip.bitingdeath.bitingdeath.mapping;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.bitingdeath.rules.MapRules;

/**
 * @author LogicalKip
 * A Zone with no -or very few- trees. Grass, small animals. Things can be seen from far away.
 */
public class PlainZone extends Zone {

	/**
	 * CONSTRUCTOR
	 * @param name The full name of the Zone, maybe including a subname. 
	 * if null, a random name is chosen
	 */
	public PlainZone(String name) {
		super(name, KindOfZone.PLAIN);
	}
	
	/**
	 * CONSTRUCTOR
	 * Name isn't given, then getting a random one
	 */
	public PlainZone() {
		this(null);
	}

	/* (non-Javadoc)
	 * @see com.logicalkip.bitingdeathandroid.bitingdeath.mapping.Zone#zombiesAtStart()
	 */
	@Override
	protected int zombiesAtStart() {
		return MapRules.PLAIN_NB_ZOMBIES_DEFAULT + BitingDeathGame.getRandomInt(-2, +2);
	}

	/* (non-Javadoc)
	 * @see com.logicalkip.bitingdeathandroid.bitingdeath.mapping.Zone#getRandomName()
	 */
	@Override
	protected String getRandomName() {
		return "Plain n°" + (int) (Math.random() * 100);
	}

}
