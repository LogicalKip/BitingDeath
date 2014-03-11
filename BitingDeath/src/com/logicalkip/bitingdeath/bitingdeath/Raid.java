package com.logicalkip.bitingdeath.bitingdeath;

import java.util.Iterator;
import java.util.LinkedList;

import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;
import com.logicalkip.bitingdeath.exceptions.CantRunRaidBecause;
import com.logicalkip.bitingdeath.exceptions.CantRunRaidException;
import com.logicalkip.bitingdeath.exceptions.IncoherentNumberException;

/**
 * A scavenging mission.
 * Featuring destination, the raiding team, etc, 
 * and info about the raid once it has been run (survivors hurt, etc).
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
	 * Those messages shall be added to the ones in BitingDeathGame, and displayed somehow/when after the raid
	 * Ex : "John was bitten", "Nothing was found".
	 */
	protected LinkedList<String> messagesToDisplayOnceRaidIsOver;
	
	/**
	 * Sublist of this.team, it contains survivors that were hurt (killed ?) last time running the raid.
	 * This is purely informative, and those in this list can definitely still be in this.team
	 */
	protected LinkedList<Survivor> survivorsHurtDuringRaid;
	
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
	public Raid(LinkedList<Survivor> team, Zone zoneDest) {
		this(new RaidSettings(zoneDest, team));
	}
	
	/**
	 * @param raidSettings2
	 */
	public Raid(RaidSettings raidSettings) {
		this.raidSettings = raidSettings;
		this.loot = 0;
		this.messagesToDisplayOnceRaidIsOver = null;
		this.survivorsHurtDuringRaid = null;
	}

	/**
	 * Run the raid. Beware, as people may get hurt and/or nothing found.
	 * CURRENT RULES : 0.5 chance per survivor to get 1 ration, 0.25 to get 2 and 0.25 nothing.
	 * Replace messagesToDisplayOnceRaidIsOver with another list (might be empty)
	 * Replace survivorsHurtDuringRaid with the unlucky fellows. Only writing down hurt survivors, no deleting or turning.
	 * Sets loot to its new value (what will be brought back to the camp).
	 * Remove killed zombies from the raided zone.
	 * @throws CantRunRaidException when no destination or team provided
	 * @see com.logicalkip.bitingdeathandroid.bitingdeath.Raid#chancesOfSurvivingTheRaid(Survivor, int)
	 */
	public void run() throws CantRunRaidException { 
		if (this.raidSettings.getDestination() == null)
			throw new CantRunRaidException(CantRunRaidBecause.DESTINATION_NULL);
		
		if (this.raidSettings.getTeam() == null || this.raidSettings.getTeam().isEmpty())
			throw new CantRunRaidException(CantRunRaidBecause.NO_SURVIVORS);
		
		
		int lootFound = 0, zombiesKilled = 0, zombiesRoamingTheZone = this.raidSettings.getDestination().getZombiesLeft(), nbOfSurvivorsAtFirst = this.raidSettings.getTeam().size();
		double rand;
		
		Iterator<Survivor> iter = this.raidSettings.getTeam().iterator();
		
		this.messagesToDisplayOnceRaidIsOver = new LinkedList<String>();
		this.survivorsHurtDuringRaid = new LinkedList<Survivor>();

		//TODO looting before or after killing zombies (% danger) ? 
		while (iter.hasNext()) {
			Survivor currentSurvivor = iter.next();
			if (BitingDeathGame.getRandomProbability() > chancesOfSurvivingTheRaid(currentSurvivor, zombiesRoamingTheZone))
			{// Too bad.
				this.survivorsHurtDuringRaid.add(currentSurvivor);
				this.messagesToDisplayOnceRaidIsOver.add(currentSurvivor.getName() + " has been bitten while running the raid");
			}
			
			else // Still alive, may proceed to scavenging
			{
				rand = BitingDeathGame.getRandomProbability();
				if (rand > 0.75)
					lootFound += 2;
				else if (rand > 0.25)
					lootFound++;
			}
		}
		
		// LOOT !
		this.loot = lootFound;
		if (lootFound == 0)
			this.messagesToDisplayOnceRaidIsOver.add("Nothing interesting was brought back from " + this.raidSettings.getDestination().getName());
		
		// Batching some Zeds, even hurt survivors did.
		if (nbOfSurvivorsAtFirst > zombiesRoamingTheZone)
			zombiesKilled = zombiesRoamingTheZone;
		else
			zombiesKilled = nbOfSurvivorsAtFirst;

		if (zombiesKilled > 0)
		{			
			if (zombiesKilled == 1)
				this.messagesToDisplayOnceRaidIsOver.add("One unlucky zombie was killed");
			else 
				this.messagesToDisplayOnceRaidIsOver.add(zombiesKilled + " zombies have been killed");
			
			iter = this.raidSettings.getTeam().iterator();
			while(iter.hasNext())
				iter.next().improveFightingSkill();
		}
		
		try {
			this.raidSettings.getDestination().removeZombies(zombiesKilled);
		} catch (IncoherentNumberException e) {
			System.err.println("Erreur de code dans Raid/run : le nombre de zombies à supprimer est incohérent");
			e.printStackTrace();
		}
		
	}

	/**
	 * Return a double between 0 (excluded, we never know =P) and 1 (if there are no zombies), 
	 * higher if survivor is more likely to survive the raid.
	 * More people in the team means much higher chances.
	 * @param survivor His personnal attributes might change the deal.
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
			
			survivingChances = 0.99 / (zombiesUsedInCalculing / this.raidSettings.getTeam().size());
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
	
	
	/**
	 * Regular toString method
	 */
	public String toString() {
		String res = "";
		res += this.raidSettings.getTeam().size() + " survivors : \n";
		Iterator <Survivor> i = this.raidSettings.getTeam().iterator();
		while (i.hasNext())
			res += "\t" + i.next().toString() + "\n";
		res += "Loot : " + this.loot;
		return res;
	}

	/**
	 * @return the survivorsHurtDuringRaid
	 */
	public LinkedList<Survivor> getSurvivorsHurtDuringRaid() {
		return this.survivorsHurtDuringRaid;
	}

	/**
	 * @return the messagesToDisplayOnceRaidIsOver
	 */
	public LinkedList<String> getMessagesToDisplayOnceRaidIsOver() {
		return this.messagesToDisplayOnceRaidIsOver;
	}

	/**
	 * @return the destination
	 */
	public Zone getDestination() {
		return this.raidSettings.getDestination();
	}
}
