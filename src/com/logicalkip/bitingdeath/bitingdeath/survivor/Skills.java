package com.logicalkip.bitingdeath.bitingdeath.survivor;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;

/**
 * Survivor skills. They improve when used, up to Skills.LEVEL_MAX,
 * and represent how skilled a survivor is
 * in a field : fighting, scavenging, etc.
 * Every survivor has its own Skills.
 * @author LogicalKip
 */
public class Skills {
	
	/**
	 * Every skill is between LEVEL_MIN and this.
	 * No one can get better than LEVEL_MAX.
	 */
	public final static int LEVEL_MAX = 100;
	
	/**
	 * Every skill is between LEVEL_MAX and this.
	 * The worse skill level imaginable would be LEVEL_MIN.
	 */
	public final static int LEVEL_MIN = 1;
	
	/**
	 * No survivor will start the game being worse than STARTING_MIN_LEVEL.
	 * This is true with any skill.
	 * Must be < LEVEL_MAX. No kidding.
	 */
	private final static int STARTING_MIN_LEVEL = 10;
	
	/**
	 * How well a survivor fights.
	 */
	private int fightingSkill;
	
	/**
	 * How well a survivor can scavenge.
	 */
	private int scavengingSkill;
	
	
	public Skills () {
		this.fightingSkill = Skills.getRandomStartingLevel();
		this.scavengingSkill = Skills.getRandomStartingLevel();
	}
	
	
	/**
	 * Not every survivor begins with the same skill levels
	 * @return a random number between LEVEL_MIN and LEVEL_MAX,
	 * to initialize a skill.
	 */
	@SuppressWarnings("unused") // in case consts were changed, thus perhaps returning > LEVEL_MAX
	private static int getRandomStartingLevel() {
		final int INITIAL_MAX_FOR_RAND = 20 + Skills.STARTING_MIN_LEVEL;
		int max = INITIAL_MAX_FOR_RAND, min = Skills.STARTING_MIN_LEVEL;
		if (INITIAL_MAX_FOR_RAND > Skills.LEVEL_MAX) // in case of a starting level too high
			max = Skills.LEVEL_MAX;
		
		return Skills.LEVEL_MIN + BitingDeathGame.getRandomInt(min, max);
	}

	/**
	 * Slightly improve the fighting skill, if not already at max
	 */
	protected void improveFightingSkill(){
		if (this.fightingSkill < Skills.LEVEL_MAX)
			this.fightingSkill++;
	}
	
	/**
	 * Slightly improve the scavenging skill, if not already at max
	 */
	protected void improveScavengingSkill(){
		if (this.scavengingSkill < Skills.LEVEL_MAX)
			this.scavengingSkill++;
	}

	/**
	 * @return the fightingSkill
	 */
	public int getFightingSkill() {
		return this.fightingSkill;
	}


	/**
	 * @return the scavengingSkill
	 */
	public int getScavengingSkill() {
		return this.scavengingSkill;
	}
}
