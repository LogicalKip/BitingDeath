package com.logicalkip.bitingdeath.bitingdeath.survivor;

import java.util.Collection;
import java.util.LinkedList;

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
	
	private final static String maleFirstNames[] = 	{	
			"John", "Peter", "Jack", "Charles", "Philip", "Luke", "Victor", "Stefan", "Mike", "Glenn"
		};
	private final static String femaleFirstNames[] = {	
			"Sofia", "Judy", "Lois", "Jenny", "Melissa", "Eve", "Evelyn", "Angelina", "Lisa", "Amy"
		};
	private final static String surnames[] = {	
			"Kennedy", "Black", "White", "Field", "Chesterblutch", "Conway", "Reminger", 
			"Coldsnow", "Hazelnut", "Starbringer", "Griffin"
		};
	
				/* ATTRIBUTES */
	/**
	 * ID of the survivor. There must NOT be twice the same ID among survivors (whether dead or alive),
	 * as it is used to differentiate them.
	 */
	protected final int id;
	
	
	/**
	 * Name of the survivor.
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
	public Survivor() {		//TODO improve constructors hierarchy, understanding, etc.
		this("");
	}
	
	/**
	 * CONSTRUCTOR
	 * @param newName the name the survivor will be named after, or a random one if invalid.
	 */
	public Survivor(String newName) {
		this(new LinkedList<String>());
		
		if (newName != null && !newName.equals(""))
			this.name = newName;
	}
	
	/**
	 * CONSTRUCTOR
	 * @param namesNotToUse The name of the survivor will not be any of namesNotToUse's items
	 */
	public Survivor(Collection<String> namesNotToUse) {
		Survivor.survivorsCreated++;
		this.id = Survivor.survivorsCreated;
		this.isFemale = (BitingDeathGame.getRandomProbability() > 0.5);
		this.skills = new Skills();	
		
		this.name = getRandomUnusedName(namesNotToUse, this.isFemale);
		
	
	}
	
	/**
	 * @param usedNames A collection (without duplication) of names (first name + surname) that won't be chosen
	 * @param femaleName defines whether the returned name should be a female's one.
	 * @return A random name not included in usedNames. If it's not possible, then any random name.
	 */
	public String getRandomUnusedName(Collection<String> usedNames, boolean femaleName) {
		String name = Survivor.getRandomName(femaleName);
		int nbSurnames = Survivor.surnames.length, nbFirstNames;
		if (femaleName) {
			nbFirstNames = Survivor.femaleFirstNames.length;
		} else {
			nbFirstNames = Survivor.maleFirstNames.length;
		}
		
		if (usedNames.size() < nbFirstNames * nbSurnames) {
			while (usedNames.contains(name)) {
				name = Survivor.getRandomName(femaleName);
			}
		}
		return name;
	}
	
	/**
	 * A random name, composed of a first name and a surname. Ex : "John Kennedy"
	 * Might return an already used name.
	 * See also {@link Survivor#getRandomUnusedName(Collection, boolean)}
	 * @return a random name, depending on the survivor's sex, given as a parameter
	 */
	protected static String getRandomName(boolean femaleName) {
		String res = "";
		
		if (femaleName)
			res += Survivor.femaleFirstNames[BitingDeathGame.getRandomInt(0, Survivor.femaleFirstNames.length - 1)];
		else
			res += Survivor.maleFirstNames[BitingDeathGame.getRandomInt(0, Survivor.maleFirstNames.length - 1)];
		
		res += " " + Survivor.surnames[BitingDeathGame.getRandomInt(0, Survivor.surnames.length - 1)];
		
		return res;	
	}
	
	/**
	 * Regular equals class.
	 * @param otherSurvivor
	 * @return true if the id are the same
	 */
	protected boolean equals(Survivor otherSurvivor) {
		return otherSurvivor != null && this.id == otherSurvivor.id;
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
