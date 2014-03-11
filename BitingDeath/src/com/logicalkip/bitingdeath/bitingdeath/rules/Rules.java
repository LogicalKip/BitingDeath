package com.logicalkip.bitingdeath.bitingdeath.rules;


/**
 * Numbers ands others things ruling the game. May be changed by the user in an option menu. (...not everything)
 * @author LogicalKip
 *
 */
public class Rules {
				/* CONSTANTS */
	/**
	* How many suvrivors in the default team of a new game 
	*/
	public static final int NB_SURVIVORS_START_DEFAULT = 3;
	
	/**
	* How many rations do you begin a game with
	*/
	public static final int RATIONS_START_DEFAULT = Rules.NB_SURVIVORS_START_DEFAULT * 3;
	
	
				/* ATTRIBUTES */
	/**
	 * How many survivors you start a game with
	 */
	protected int nbSurvivorsStart;
	
	/**
	 * How many rations you start a game with
	 */
	protected int rationsStart;

	/**
	 * Rules concerning more specifically the map and its zones.
	 */
	protected MapRules mapRules;
	
				/* METHODS */
	
	public Rules() {
		this.mapRules = new MapRules();
		this.nbSurvivorsStart = Rules.NB_SURVIVORS_START_DEFAULT;
		this.rationsStart = Rules.RATIONS_START_DEFAULT;
	}


	/**
	 * @return the mapRules
	 */
	public MapRules getMapRules() {
		return this.mapRules;
	}


	/**
	 * @return the nbSurvivorsStart
	 */
	public int getNbSurvivorsStart() {
		return this.nbSurvivorsStart;
	}


	/**
	 * @param nbSurvivorsStart the nbSurvivorsStart to set
	 */
	public void setNbSurvivorsStart(int nbSurvivorsStart) {
		this.nbSurvivorsStart = nbSurvivorsStart;
	}
	


	/**
	 * @return the rationsStart
	 */
	public int getRationsStart() {
		return this.rationsStart;
	}


	/**
	 * @param rationsStart the rationsStart to set
	 */
	public void setRationsStart(int rationsStart) {
		this.rationsStart = rationsStart;
	}
	
}
