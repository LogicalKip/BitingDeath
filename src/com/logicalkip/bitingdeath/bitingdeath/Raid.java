package com.logicalkip.bitingdeath.bitingdeath;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Skills;
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
	 * TODO the survivors are supposedly looting together, which is therefore safer but gets less loot.
	 * Replace {@link Raid#messagesToDisplayOnceRaidIsOver} with another list (might be empty)
	 * Replace {@link Raid#survivorsHurtDuringRaid} with the unlucky fellows  (might be empty). Only writing down hurt survivors, no deleting or turning.
	 * Replace {@link Raid#newSurvivorsFound} with the survivors found during this raid
	 * Sets {@link Raid#loot} to its new value (what will be brought back to the camp).
	 * Remove killed zombies from the raided zone.
	 * @throws CantRunRaidException when no destination or team provided
	 * @see {@link Raid#chancesOfSurvivingTheRaid(Survivor, int)}
	 */
	public void run() throws CantRunRaidException { 
		if (this.raidSettings.getDestination() == null)
			throw new CantRunRaidException(CantRunRaidBecause.DESTINATION_NULL);
		if (this.raidSettings.getTeam() == null || this.raidSettings.getTeam().isEmpty())
			throw new CantRunRaidException(CantRunRaidBecause.NO_SURVIVORS);
		
		final int TOO_MANY_ZOMBIES = 10;//TODO parameter that can be changed by the user

		int zombiesKilled, zombiesInWholeZoneAtFirst = this.raidSettings.getDestination().getZombiesLeft();
		this.messagesToDisplayOnceRaidIsOver = new LinkedList<String>();
		this.survivorsHurtDuringRaid = new LinkedList<Survivor>();
		this.newSurvivorsFound = new LinkedList<Survivor>();
		
		int zombiesAboutToEncouter = someOfThem(zombiesInWholeZoneAtFirst);
		
		if (zombiesAboutToEncouter >= TOO_MANY_ZOMBIES) {
			nopeNopeNope(zombiesAboutToEncouter);
		} else {
			zombiesKilled = this.clearArea(zombiesAboutToEncouter);
			this.loot = this.lootArea();

			this.addLootRelatedMessages();
			this.addZombieRelatedMessages(zombiesAboutToEncouter, zombiesKilled);
			
			encouterNPCs();
			
			improveSkills(zombiesKilled);
		}
	}
	
	/**
	 * @param nbZombies >= return value >= 0. "them"
	 * @return a number representing a subgroup of the zombies
	 */
	int someOfThem(int nbZombies) {
		return (int) Math.ceil(Math.random() * nbZombies);
	}
	
	/**
	 * The team thinks it's better to not even try to loot, because they have seen too many zombies wandering around.
	 * @param z the number of zombies seen by the team in the zone before looting
	 */
	private void nopeNopeNope(int z) {				
		int estimatedFloor = (int) Math.floor(((double)z) / 10.0);
		estimatedFloor *= 10;
		
		this.messagesToDisplayOnceRaidIsOver.add("It seemed safer to retreat, there were maybe " + estimatedFloor + " or " + (estimatedFloor + 10) + " zeds !");
	}
	
	/**
	 * Adds messages to {@link Raid#messagesToDisplayOnceRaidIsOver} concerning the loot freshly found
	 */
	private void addLootRelatedMessages() {
		if (this.loot == 0)
			this.messagesToDisplayOnceRaidIsOver.add("Nothing was found in " + this.raidSettings.getDestination().getName());
	}
	
	/**
	 * Adds messages to {@link Raid#messagesToDisplayOnceRaidIsOver} concerning the zombies encountered
	 * @param zombiesEncoutered How many zombies were met by the team
	 * @param zombiesKilled  How many zombies were killed by the team
	 */
	private void addZombieRelatedMessages(int zombiesEncoutered, int zombiesKilled) {
		if (zombiesEncoutered == 0)
			this.messagesToDisplayOnceRaidIsOver.add("No zombies were encoutered");
		else if (zombiesKilled == 1)
			this.messagesToDisplayOnceRaidIsOver.add("One lonely zombie has been killed");
		else 
			this.messagesToDisplayOnceRaidIsOver.add(zombiesKilled + " zombies have been killed");
	}
	
	/**
	 * Gives a chance to every survivor in the raid to scavenge something
	 * @return how much food was found
	 */
	private int lootArea() {
		int lootFound = 0;
		for (Survivor currentSurvivor : this.raidSettings.getTeam()) {
			final int X = currentSurvivor.getSkills().getScavengingSkill(), MIN = Skills.LEVEL_MIN, MAX = Skills.LEVEL_MAX; 
			double coeff = 1 + ((X - MIN) / ((double) MAX - MIN));
			double rand = BitingDeathGame.getRandomProbability() * coeff;
			if (rand > 1) {
				rand = 1;
			}
			
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
	 * 
	 * FIXME make it possible to die without returning home, and not "kill all zombies no matter what". Update messages.
	 * Even one z can "eat" a survivor alone (big neck bite, won't go anywhere)
	 * Can't get new survivors back if all eaten
	 * Dead if nbzombies/surv > N ? Does that mean same fate for everyone ?
	 * Bitten if getProb < N and dead if getProb << N ?
	 * One dead, still can loot ? must kill them before ?
	 * notComingBackToBase != dead ? (left, but still alive somewhere, maybe with NPCs))
	 * two lists : hurtDuringRaid (bitten, maybe rename) and diedDuringRaid ?
	 * rename chanceofSURVIVingraid ? (bitten != dead)
	 * kill how many Z ?
	 * 
	 * 
	 * @param zombiesAboutToEncouter the zombies that will be fighting the raiding team
	 * 
	 * @return the number of zombies killed
	 */
	private int clearArea(int zombiesAboutToEncouter) {
		int zombiesKilled = 0;
		List<Survivor> team = this.raidSettings.team;
		
		for (int i = 0 ; i < zombiesAboutToEncouter ; i++) {
			Survivor target = team.get(Math.abs(new Random().nextInt()) % team.size());
			
			double zombieAttack = BitingDeathGame.getRandomProbability();
			
			boolean targetSaved = false;
			for (Survivor potentialSaver : team)  { // Possibly the target himself
				if (potentialSaver.getFightingEfficiency() > zombieAttack) {
					targetSaved = true;
				}
			}
			
			if (! targetSaved) {  // Bitten
				if (! this.survivorsHurtDuringRaid.contains(target)) {
					this.survivorsHurtDuringRaid.add(target);
				}
			}
			
			zombiesKilled++;
		}

		try {
			this.raidSettings.getDestination().removeZombies(zombiesKilled);
		} catch (IncoherentNumberException e) {
			System.err.println("Erreur de code dans Raid#clearArea : le nombre de zombies à supprimer est incohérent");
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
