package com.logicalkip.bitingdeath.bitingdeath;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.logicalkip.bitingdeath.bitingdeath.mapping.Base;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Map;
import com.logicalkip.bitingdeath.bitingdeath.rules.Rules;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;
import com.logicalkip.bitingdeath.exceptions.AlreadyThereException;
import com.logicalkip.bitingdeath.exceptions.CantEquipWeaponException;
import com.logicalkip.bitingdeath.exceptions.CantRunRaidException;
import com.logicalkip.bitingdeath.exceptions.NoWeaponException;


/* 
 * TODO /!\ Idea list to improve the game : 
 * Being able to build stuff on zones (barricades, bridges in swamp, towers (not the auto-shoot style, but rather to improve sight/accuracy), etc) 
 * 
 * Finding weapons
 * 
 * NPC : Limited amount of survivors hiding in every zone. May not want to come along. Personalities. Meta-game to make them fight zombies and possibly die(turn)
 * 
 * Scout missions with approximative intell. (the more zombies, the less accurate)
 * 
 * Limited amount of food per zone (respawn ?)
 * 
 * Z attacking the base (not/hardly spawned ?)
 * 
 * Choice (when)to end a bitten survivor's life. Turning in the base if not done quick enough. What if no weapons available ^^ ?
 * 
 * Strategies/philosophies to choose and to follow : come back if there are too many Z/get in there no matter what, etc (Strategy design pattern)
 * 
 * Find a way to be sure that a dying survivor is removed from all lists/var, even a forgotten or not so useful one (leader of survivors, last team used, raids, ...)
 * 		NOTES :
 * 			faire ma classe survlist qui hérite de list<surv>, qui possède en attribut static les survlist ainsi créées
 * 			(via new), et une méthode static qui supprime les surv de toutes les survlist existantes. 
 * 			pb : et les variables simples ? et si on renew une liste (dans une autre classe), l'ancienne sera toujours
 * 			dans survlist.attributstatic et continuera d'etre updatée ? 
 * 			
 * Counting how many times a Zone has been raided
 */



/**
 * A zombie game mostly made of options and menus. Sending survivors scavenging, etc.
 * This class does not feature display and should be instanced or extended for real use.
 * @author LogicalKip
 *
 */
public class BitingDeathGame {

				/* ATTRIBUTES */
	/**
	 * Number of rations (for now, 1 survivor needs 1 ration a day)
	 */
	private int rations;
	
	/**
	 * Alive survivors in the team
	 */
	private List<Survivor> survivors;
	
	/**
	 * Day 0, Day 1, ... since the beginning of the game
	 */
	private int currentDay;
	
	/**
	 * The raids as they will be run for the current day.
	 * /!\ You must not put a Survivor in several raids at once.
	 * No checking will be made.
	 */
	private List<RaidSettings> plannedRaids;
	
	/**
	 * If the game is lost.
	 */
	private boolean gameOver;

	/**
	 * Ex : "All survivors are dead". Should be displayed when the game is lost.
	 */
	private String gameOverMessage;
	
	/**
	 * The messages that must be displayed to the player. Ex : "X died during a mission", "X has gone because no food left", etc...
	 * They must be displayed in FIFO order, and then removed from the list.
	 * This does not include any game-over message. See {@link BitingDeathGame#gameOverMessage}
	 */
	private List<String> messagesToDisplay;
	
	/**
	 * The map of the game, made of different Zones
	 */
	private Map map;
	
	private Rules rules;
	
	private Base mainBase;
	
				/* METHODS */
	/**
	 * CONSTRUCTOR
	 */
	public BitingDeathGame() {
		this.rules = new Rules();
		this.rations = this.rules.getRationsStart();
		this.survivors = this.getRandomSurvivorList();
		this.currentDay = 0;
		this.gameOver = false;
		this.gameOverMessage = null;
		this.messagesToDisplay = new LinkedList<String>();
		this.messagesToDisplay.add("Welcome ! Survive as long as you can !");		
		this.map = new Map(this.rules.getMapRules().getMapWidth(), this.rules.getMapRules().getMapHeight());
		this.plannedRaids = new LinkedList<RaidSettings>();
		
		Random r = new Random();
		this.mainBase = new Base(r.nextInt(this.map.getWidth()), r.nextInt(this.map.getHeight()));
	}

	/**
	 * Returns a new list of N random survivors, N being defined in this.rules
	 */
	private List<Survivor> getRandomSurvivorList() {
		List<Survivor> survivorList = new LinkedList<Survivor>();
		List<String> namesUsed = new LinkedList<String>();
		for (int i = 0 ; i < this.rules.getNbSurvivorsStart() ; i++) {
			Survivor s = new Survivor(namesUsed);
			namesUsed.add(s.getName());
			survivorList.add(s);
		}
		return survivorList;
	}
	

	
	/**
	 * When the player ends the current day.
	 * @throws CantRunRaidException 
	 */
	public void nextDay() throws CantRunRaidException {
		for (RaidSettings currRaid : this.plannedRaids)
			this.runRaid(currRaid);
				
		this.removeEmptyRaids();
		
		this.map.randomZombieRoaming();
		this.rations -= this.survivors.size(); // Eating during evening
		
		this.currentDay++; // Cock-a-doodle-do !
		
		
		if (this.survivors.isEmpty())
			this.gameOver("No more survivors");
		else if (this.rations < 0)
			this.gameOver("No more rations");		
	}
	
	/**
	 * Sets this.gameOver to true and a value to gameOverMessage, including the reason
	 * @param reason should not contain "game over"
	 */
	private void gameOver(String reason) {
		this.gameOver = true;
		this.gameOverMessage = "GAME OVER";
		if (reason != null && ! reason.equals(""))
			this.gameOverMessage += " : " + reason + "\nYou survived " + this.currentDay + " days.";
	}
	
	/**
	 * Run a raid with raidSettings and add found rations to current owned ones.
	 * Dead survivors will be removed from the game and added as zombies in the raided Zone.
	 * @param raidSettings Must already be created and initialized : team, etc.
	 * @throws CantRunRaidException 
	 */
	private void runRaid(RaidSettings raidSettings) throws CantRunRaidException {
		Raid raid = new Raid(raidSettings);
		
		raid.run();
		
		this.messagesToDisplay.addAll(raid.getMessagesToDisplayOnceRaidIsOver());
		List<Survivor> deadSurvivors = raid.getSurvivorsHurtDuringRaid();
		
		// Removing dead survivors from the game
		for (Survivor poorBastard : deadSurvivors) { 
			this.addMessageToDisplay(poorBastard.getName() + " has been bitten during the raid. " +
									(poorBastard.isFemale() ? "She" : "He") +
									" was put out of " +
									(poorBastard.isFemale() ? "her" : "his") + " misery."
					);
			
			// Get weapon back (if any)
			if (poorBastard.getWeapon() != null) {
				this.mainBase.getAvailableWeapons().add(poorBastard.getWeapon());
				poorBastard.setWeapon(null);
			}

			this.removeSurvivorFromGame(poorBastard);
		}
		
		for (Survivor newSurvivor : raid.getNewSurvivorsFound()) {
			try {
				this.recruitNewSurvivor(newSurvivor);
			} catch (AlreadyThereException e) {
				e.printStackTrace();
				this.addMessageToDisplay(e.getMessage());
			}
		}
		
		this.rations += raid.getLoot();
	}
	
	/**
	 * Add a message to a list, waiting to be displayed to the player. (FIFO order)
	 * @param msg Does nothing if null
	 */
	public void addMessageToDisplay(String msg) {
		if (msg != null)
			this.messagesToDisplay.add(msg);
	}
	
	/**
	 * @return Return true if the FIFO list of messages-to-display-to-the-user is not empty
	 */
	public boolean thereAreMessagesToDisplay() {
		return ! this.messagesToDisplay.isEmpty();
	}
	
	/**
	 * Remove the first message-to-display from the FIFO list and return it.
	 * @return the first message of the current list of messages to display, or null if the list is empty
	 */
	public String getNextMessageToDisplay() {
		String res = null;
		if (this.thereAreMessagesToDisplay()) {
			res = this.messagesToDisplay.get(0); // First
			this.messagesToDisplay.remove(0);
		}
		return res;
	}
	
	/**
	 * TODO Constant checking : when adding another Survivor container (list, attribute, etc), make sure this method will remove it (or set to null when an attribute) too.
	 * Remove a survivor (presumably when dead) from all lists referencing him and sets all attributes pointing it to null
	 * @param s The survivor to remove
	 */
	private void removeSurvivorFromGame(Survivor s) {
		this.survivors.remove(s);
		
		for (RaidSettings currRaid : this.plannedRaids) {
			currRaid.team.remove(s);
		}
	}
	
	/**
	 * Remove from the planned raids those whose Survivor list is empty.
	 * (Creates a new list without the empty raids)
	 */
	private void removeEmptyRaids() {
		List<RaidSettings> newRaidList = new LinkedList<RaidSettings>();
		for (RaidSettings raid : this.plannedRaids) {
			if (! raid.getTeam().isEmpty()) {
				newRaidList.add(raid);
			}
		}
		this.plannedRaids = newRaidList;
	}
	
	/**
	 * Adds a new survivor to the team
	 * @param s the new survivor
	 */
	public void recruitNewSurvivor(Survivor s) throws AlreadyThereException {
		if (this.survivors.contains(s)) {
			throw new AlreadyThereException(s.getName() + " is already a part of the crew");
		} else {
			this.survivors.add(s);
		}
	}
	
	/**
	 * Makes survivor equip targeted weapon from the main base and stores the (if any) previously equipped weapon in base
	 * @param survivor must be in {@link BitingDeathGame#survivors}
	 * @param weapon must be in base and not already used by a survivor {@link Base#getAvailableWeapons()}
	 * @throws CantEquipWeaponException if any of the above is not respected
	 */
	public void equipWeapon(Survivor survivor, Weapon weapon) throws CantEquipWeaponException {
		if (! this.survivors.contains(survivor)) {
			throw new CantEquipWeaponException(CantEquipWeaponException.notInTheTeam(survivor));
		} else if (! this.mainBase.getAvailableWeapons().contains(weapon)) {
			throw new CantEquipWeaponException("Weapon must be one of those stored in base to equip");
		} else {
			Survivor owner = this.alreadyEquipped(weapon);
			if (owner != null) { 
				throw new CantEquipWeaponException("Weapon is already equipped by " + owner.getName());
			} else {
				Weapon oldWeapon = survivor.getWeapon();
				
				this.mainBase.getAvailableWeapons().remove(weapon);
				survivor.setWeapon(weapon);
				
				if (oldWeapon != null) {
					this.mainBase.getAvailableWeapons().add(oldWeapon);
				}
			}
		}
	}
	
	/**
	 * Makes the survivor take off his/her current Weapon and stores it in the main base
	 * @param s the survivor whose weapon must be unequipped
	 * @throws CantEquipWeaponException if the survivor isn't in {@link BitingDeathGame#survivors}
	 * @throws NoWeaponException if the survivor has no weapon to take off
	 */
	public void unequipWeapon(Survivor s) throws CantEquipWeaponException, NoWeaponException {
		Weapon w = s.getWeapon();
		if (! this.survivors.contains(s)) {
			throw new CantEquipWeaponException(CantEquipWeaponException.notInTheTeam(s));
		} else if (w == null) {
			throw new NoWeaponException();
		} else {
			s.setWeapon(null);
			this.mainBase.getAvailableWeapons().add(w);
		}
	}
	
	/**
	 * Returns a (hopefully the) survivor from the team having that weapon equipped or null if equipped by no one in {@link BitingDeathGame#survivors}
	 */
	private Survivor alreadyEquipped(Weapon weapon) {		
		for (Survivor s : this.survivors) {
			if (s.getWeapon() == weapon) {
				return s;
			}
		}
		return null;
	}
	
	
				/* GETTERS | SETTERS */
	
	/**
	 * Return the current day (0, 1, ...)
	 */
	public int getCurrentDay() {
		return this.currentDay;
	}
	
	/**
	 * Return true if the game is lost.
	 */
	public boolean getGameOver() {
		return this.gameOver;
	}
	
	/**
	 * Return the game over message, which should be null if the game isn't lost
	 */
	public String getGameOverMessage() {
		return this.gameOverMessage;
	}
	
	/**TODO this and the double one in a RandomGenerator class ?
	 * Return a random integer between min and max (including both)
	 */
	public static int getRandomInt(int min, int max) {
		return (int)(Math.random() * (max+1-min)) + min;
	}
	
	/** 
	 * Return a random double between 0 and 1. Might be useful for luck tests.
	 */
	public static double getRandomProbability() {
		return Math.random();
	}

	/**
	 * @return the survivors. Not the very list of survivors owned by the game, but a list (copy)
	 * with those survivors.
	 */
	public List<Survivor> getSurvivors() {
		return new LinkedList<Survivor>(this.survivors);
	}

	/**
	 * @return the map
	 */
	public Map getMap() {
		return this.map;
	}


	/**
	 * @return the currentRaidSettings
	 */
	public List<RaidSettings> getCurrentPlannedRaids() {
		return this.plannedRaids;
	}


	/**
	 * @param currentRaidSettings the currentRaidSettings to set
	 */
	public void setCurrentRaidSettings(List<RaidSettings> currentRaidSettings) {
		this.plannedRaids = currentRaidSettings;
	}

	public int getRations() {
		return rations;
	}

	/**
	 * @return the mainBase
	 */
	public Base getMainBase() {
		return mainBase;
	}

	/**
	 * @param mainBase the mainBase to set
	 */
	public void setMainBase(Base mainBase) {
		this.mainBase = mainBase;
	}	
	
}
