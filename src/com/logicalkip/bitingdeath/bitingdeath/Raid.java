package com.logicalkip.bitingdeath.bitingdeath;

import java.util.LinkedList;
import java.util.List;

import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;
import com.logicalkip.bitingdeath.exceptions.CantRunRaidBecause;
import com.logicalkip.bitingdeath.exceptions.CantRunRaidException;
import com.logicalkip.bitingdeath.exceptions.IncoherentNumberException;

/**
 * A scavenging mission.
 * Featuring destination, the raiding team, etc, 
 * and info about the raid once it has been run (hurt survivors, etc).
 * @author LogicalKip
 *
 */
public class Raid {

	/* ATTRIBUTES */ 	
	/**
	 * Info chosen by the player : which survivors, raiding which Zone...
	 */
	protected RaidSettings raidSettings; 

	/**
	 * Last found loot in this raid.
	 */
	protected int loot;

	/**
	 * Those messages shall be added to the ones in {@link BitingDeathGame}, and displayed somehow, after the raid
	 * Ex : "John was bitten", "Nothing was found".
	 */
	protected List<String> messagesToDisplayOnceRaidIsOver;

	/**
	 * Sublist of this.team, it contains survivors that were hurt (killed ?) last time running the raid.
	 * This is purely informative, and those in this list can definitely still be in this.team
	 */
	protected List<Survivor> survivorsHurtDuringRaid;

	/**
	 * A list of survivors found when running the raid
	 */
	protected List<Survivor> newSurvivorsFound;

	/* METHODS */

	/**
	 * CONSTRUCTOR
	 * Create a new empty team, and no destination.
	 */
	public Raid() {
		this(new LinkedList<Survivor>(), null);
	}

	/**
	 * CONSTRUCTOR
	 * @param team Create a new empty team if null
	 */
	public Raid(List<Survivor> team, Zone zoneDest) {
		this(new RaidSettings(zoneDest, team));
	}

	/**
	 * CONSTRUCTOR
	 * @param raidSettings
	 */
	public Raid(RaidSettings raidSettings) {
		this.raidSettings = raidSettings;
		this.loot = 0;
		this.messagesToDisplayOnceRaidIsOver = null;
		this.survivorsHurtDuringRaid = null;
		this.newSurvivorsFound = null;
	}

	/**
	 * Run the raid. Beware, as people may get hurt and/or nothing found.
	 * Replace {@link Raid#messagesToDisplayOnceRaidIsOver} with another list (might be empty)
	 * Replace {@link Raid#survivorsHurtDuringRaid} with the unlucky fellows  (might be empty). Only writing down hurt survivors, no deleting or turning.
	 * Replace {@link Raid#newSurvivorsFound} with the survivors found during this raid
	 * Sets {@link Raid#loot} to its new value (what will be brought back to the camp).
	 * Remove killed zombies from the raided zone.
	 * @throws CantRunRaidException when no destination or team provided
	 * @see com.logicalkip.bitingdeathandroid.bitingdeath.Raid#chancesOfSurvivingTheRaid(Survivor, int)
	 */
	public void run() throws CantRunRaidException { 
		if (this.raidSettings.getDestination() == null)
			throw new CantRunRaidException(CantRunRaidBecause.DESTINATION_NULL);
		if (this.raidSettings.getTeam() == null || this.raidSettings.getTeam().isEmpty())
			throw new CantRunRaidException(CantRunRaidBecause.NO_SURVIVORS);

		int zombiesKilled, zombiesAtFirst = this.raidSettings.getDestination().getZombiesLeft();
		this.messagesToDisplayOnceRaidIsOver = new LinkedList<String>();
		this.survivorsHurtDuringRaid = new LinkedList<Survivor>();
		
		zombiesKilled = this.clearArea();
		this.loot = this.lootArea();

		this.addLootRelatedMessages();
		this.addZombieRelatedMessages(zombiesAtFirst, zombiesKilled);
		
		this.encouterNPCs();
		
		this.improveSkills(zombiesKilled);
	}
	
	/**
	 * Adds messages to {@link Raid#messagesToDisplayOnceRaidIsOver} concerning the loot freshly found
	 */
	private void addLootRelatedMessages() {
		if (this.loot == 0 && this.survivorsHurtDuringRaid.size() != this.raidSettings.getTeam().size())
			this.messagesToDisplayOnceRaidIsOver.add("Nothing was found in " + this.raidSettings.getDestination().getName());
	}
	
	/**
	 * Adds messages to {@link Raid#messagesToDisplayOnceRaidIsOver} concerning the zombies encountered
	 * @param zombiesAtFirst How many zombies were in the raided zone when the team arrived
	 * @param zombiesKilled  How many zombies were killed by the team
	 */
	private void addZombieRelatedMessages(int zombiesAtFirst, int zombiesKilled) {
		if (zombiesAtFirst == 0)
			this.messagesToDisplayOnceRaidIsOver.add("No zombies were encoutered");
		else if (zombiesKilled == 1)
			this.messagesToDisplayOnceRaidIsOver.add("One lonely zombie was killed");
		else 
			this.messagesToDisplayOnceRaidIsOver.add(zombiesKilled + " zombies have been killed");
	}
	
	/**
	 * Gives a chance to every survivor in the raid to scavenge something
	 * FIXME use scavenging skill
	 * @return how much food was found
	 */
	private int lootArea() {
		int lootFound = 0;
		for (@SuppressWarnings("unused") Survivor currentSurvivor : this.raidSettings.getTeam()) {
			double rand = BitingDeathGame.getRandomProbability();
			if (rand > 0.75)
				lootFound += 2;
			else if (rand > 0.25)
				lootFound++;
		}
		return lootFound;
	}

	/**
	 * Kill the zombies (before looting) and removes them from the zone.
	 * Survivors may be bitten, but not overwhelmed and eaten. They will still be able to return home with the loot.
	 * FIXME make it possible to die without returning home, and not "kill all zombies no matter what". Update messages.
	 * @return the number of zombies killed
	 */
	private int clearArea() {
		int zombiesAtFirst = this.raidSettings.getDestination().getZombiesLeft();
		int zombiesKilled;
		for (Survivor s : this.raidSettings.getTeam()) {
			if (BitingDeathGame.getRandomProbability() > chancesOfSurvivingTheRaid(s, zombiesAtFirst)) {
				// Bitten
				this.survivorsHurtDuringRaid.add(s);
			} 
		}
		zombiesKilled = zombiesAtFirst;

		try {
			this.raidSettings.getDestination().removeZombies(zombiesKilled);
		} catch (IncoherentNumberException e) {
			System.err.println("Erreur de code dans Raid#run : le nombre de zombies à supprimer est incohérent");
			e.printStackTrace();
		}

		return zombiesKilled;
	}

	/**
	 * Gives a chance to meet and recruit other survivors in the raided zone
	 */
	private void encouterNPCs() {
		this.newSurvivorsFound = new LinkedList<Survivor>();
		// A wild survivor appears !
		if (BitingDeathGame.getRandomProbability() < 0.25) {
			Survivor newSurvivor = new Survivor();
			this.newSurvivorsFound.add(newSurvivor);
			this.messagesToDisplayOnceRaidIsOver.add(newSurvivor.getName() + " was found !");
		}
	}

	/**
	 * Improve the skills of each survivor, according to what was done
	 * @param zombiesKilled the number of zombies killed (globally) during the raid
	 */
	private void improveSkills(int zombiesKilled) {
		for (Survivor currSurvivor : this.raidSettings.getTeam()) {
			if (zombiesKilled > 0)
				currSurvivor.improveFightingSkill();
			currSurvivor.improveScavengingSkill();
		}
	}

	/**
	 * Return a double between 0 (excluded, we never know =P) and 1 (if there are no zombies), 
	 * higher if survivor is more likely to survive the raid.
	 * More people in the team means much higher chances.
	 * @param survivor His personal attributes might change the deal.
	 * @param actualZombiesInTheZone The more, the riskier ! Above 100, things are considered the same.
	 * @return
	 */
	private double chancesOfSurvivingTheRaid(Survivor survivor, int actualZombiesInTheZone) {
		int zombiesUsedInCalculing = actualZombiesInTheZone;
		double survivingChances;
		if (actualZombiesInTheZone == 0)
			survivingChances = 1;
		else
		{
			if (actualZombiesInTheZone > 100)
				zombiesUsedInCalculing = 100;

			survivingChances = (0.99 / 
					(zombiesUsedInCalculing / this.raidSettings.getTeam().size())   
					)
					* survivor.getFightingEfficiency();
		}

		return survivingChances;
	}

	/**
	 * Add a Survivor to the team.
	 * @param newSurvivor
	 */
	public void addSurvivor(Survivor newSurvivor) {
		this.raidSettings.getTeam().add(newSurvivor);
	}

	/**
	 * Remove the specified survivor from the team. Does nothing if the param is null or not in the list.
	 * @param survivorToRemove must "equals" the one in the list to remove. 
	 */
	public void removeSurvivor(Survivor survivorToRemove) {
		this.raidSettings.getTeam().remove(survivorToRemove);
	}



	/* GETTERS | SETTERS */

	/**
	 * Return the loot. 
	 * @return 0 if nothing found or the raid is still not run.
	 */
	public int getLoot() {
		return this.loot;
	}

	@Override
	public String toString() {
		String res = "";
		res += this.raidSettings.getTeam().size() + " survivors : \n";
		for (Survivor i : this.raidSettings.getTeam())
			res += "\t" + i.toString() + "\n";
		res += "Loot : " + this.loot;
		return res;
	}

	/**
	 * @return the survivorsHurtDuringRaid
	 */
	public List<Survivor> getSurvivorsHurtDuringRaid() {
		return this.survivorsHurtDuringRaid;
	}

	/**
	 * @return the messagesToDisplayOnceRaidIsOver
	 */
	public List<String> getMessagesToDisplayOnceRaidIsOver() {
		return this.messagesToDisplayOnceRaidIsOver;
	}

	/**
	 * @return the destination
	 */
	public Zone getDestination() {
		return this.raidSettings.getDestination();
	} 

	public List<Survivor> getNewSurvivorsFound() {
		return newSurvivorsFound;
	}
}
