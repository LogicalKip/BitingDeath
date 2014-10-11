package com.logicalkip.bitingdeath.bitingdeath.mapping;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.bitingdeath.rules.MapRules;

/**
 * A Zone mostly made of trees. You don't say.
 * @author LogicalKip
 */
public class ForestZone extends Zone {	
	/**
	 * CONSTRUCTOR
	 * @param name The full name of the Zone, maybe including a subname. 
	 * if null, a random name is chosen
	 */
	public ForestZone(String name) {
		super(name, KindOfZone.FOREST);
		/* Other stuff */
	}
	
	/**
	 * CONSTRUCTOR
	 * Name isn't given, then getting a random one
	 */
	public ForestZone() {
		this(null);
	}

	/**
	 * Return the number of zombie in this Zone when the game starts
	 */
	@Override
	protected int zombiesAtStart() {		
		return MapRules.FOREST_NB_ZOMBIES_DEFAULT + BitingDeathGame.getRandomInt(-3, +3);
	}

	/* (non-Javadoc)
	 * @see com.logicalkip.bitingdeathandroid.bitingdeath.mapping.Zone#getRandomName()
	 */
	@Override
	protected String getRandomName() {
		return "Forest n�" + (int) (Math.random() * 100);
	}
	

 }
