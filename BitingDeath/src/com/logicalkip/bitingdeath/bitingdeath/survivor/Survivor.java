package com.logicalkip.bitingdeath.bitingdeath.survivor;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;

/**
 * A survivor and everything related to him/her. Current weapon, HP, etc.
 * @author LogicalKip
 *
 */
public class Survivor {
	
	/**
	 * Must be incremented whenever a new Survivor is created.
	 * Will be used to create IDs to distinguish survivors. 
	 */
	protected static int survivorsCreated = 0;
	
				/* ATTRIBUTES */
	/**
	 * ID of the survivor. There must NOT be twice the same ID among survivors (whether dead or alive),
	 * as it is used to differenciate them.
	 * 
	 * Will also be used for creating names. Ex : AnonymousSurvivor_0, AnonymousSurvivor_1, ... 
	 */
	protected final int id;
	
	
	/**
	 * Name of the survivor. Several survivors may share the same name, so use id rather to distinguish them.
	 */
	protected String name;
	
	/**
	 * Sex of the Survivor
	 */
	protected boolean isFemale;
	
	/**
	 * Skills of the survivors : how well he does when fighting, etc...
	 */
	protected Skills skills;
	
				/* METHODS */
	/**
	 * CONSTRUCTOR
	 * Gives a random name to the survivor.
	 */
	public Survivor() {		
		this(null);
	}
	
	/**
	 * CONSTRUCTOR
	 * @param newName the name the survivor will be named after, or a random one if invalid.
	 */
	public Survivor(String newName) {
		Survivor.survivorsCreated++;
		this.id = Survivor.survivorsCreated;
		this.isFemale = (BitingDeathGame.getRandomProbability() > 0.5);
		if (newName == null || newName.equals(""))
			this.name = Survivor.getRandomName(this.isFemale);
		else
			this.name = newName;
		this.skills = new Skills();		
	}
	
	/**
	 * A random name, composed of a first name and a surname. Ex : "John Kennedy"
	 * Might return an already used name.
	 * @return a random name, depending on the survivor's sex, given in the parameter
	 */
	protected static String getRandomName(boolean femaleName) {
		String maleFirstNames[] = 	
			{	
				"John", "Peter", "Jack", "Charles", "Philip", "Luke", "Victor", "Stefan", "Mike", "Glenn"
			};
		String femaleFirstNames[] = 
			{	
				"Sofia", "Judy", "Lois", "Jenny", "Melissa", "Eve", "Evelyn", "Angelina", "Lisa", "Amy"
			};
		String surnames[] = 	
			{	
				"Kennedy", "Black", "White", "Field", "Chesterblutch", "Conway", "Reminger", 
				"Coldsnow", "Hazelnut", "Starbringer", "Griffin"
			};
		
		String res = "";
		
		if (femaleName)
			res += femaleFirstNames[BitingDeathGame.getRandomInt(0, femaleFirstNames.length - 1)];
		else
			res += maleFirstNames[BitingDeathGame.getRandomInt(0, maleFirstNames.length - 1)];
		
		res += " " + surnames[BitingDeathGame.getRandomInt(0, surnames.length - 1)];
		
		return res;	
	}
	
	/**
	 * Regular equals class.
	 * @param otherSurvivor
	 * @return true if the id are the same
	 */
	protected boolean equals(Survivor otherSurvivor) {
		return this.id == otherSurvivor.id;
	}
	
				/* GETTERS | SETTERS */
		
	/**
	 * Return the survivor's name.
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Regular toString method
	 * TODO update toString methods whenever changes occur to the classes.
	 */
	public String toString() {
		String res = "";
		if (this.isFemale)
			res += "Mrs. ";
		else
			res += "Mr. ";
		
		res += this.name; 
		return res;
	}

	/**
	 * @return the skills
	 */
	public Skills getSkills() {
		return this.skills;
	}
	
	/**
	 * Slightly improve the fighting skill, if not already at max
	 */
	public void improveFightingSkill(){
		this.skills.improveFightingSkill();
	}
	
	/**
	 * Slightly improve the scavenging skill, if not already at max
	 */
	public void improveScavengingSkill(){
		this.skills.improveScavengingSkill();
	}

}
