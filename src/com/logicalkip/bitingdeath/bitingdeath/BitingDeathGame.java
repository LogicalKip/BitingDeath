package com.logicalkip.bitingdeath.bitingdeath;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.logicalkip.bitingdeath.bitingdeath.mapping.Map;
import com.logicalkip.bitingdeath.bitingdeath.rules.Rules;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;
import com.logicalkip.bitingdeath.exceptions.CantRunRaidException;
import com.logicalkip.bitingdeath.exceptions.IncoherentNumberException;


/* 
 * TODO idea list to improve the game : 
 * being able to build stuff on zones (barricades, bridges in swamp, towers, etc) 
 * XP on survivor's skills : fight, scavenge,... (100 max)
 * recruiting, start-alone-mode
 * find a way to be sure that a dying survivor is removed from all lists/var, even a forgotten or not so useful one (leader of survivors, last team used, raids, ...)
 * 		NOTES :
 * 			faire ma classe survlist qui hérite de list<surv>, qui possède en attribut static les survlist ainsi créées
 * 			(via new), et une méthode static qui supprime les surv de toutes les survlist existantes. 
 * 			pb : et les variables simples ? et si on renew une liste (dans une autre classe), l'ancienne sera toujours
 * 			dans survlist.attributstatic et continuera d'etre updatée ? 
 * 			
 * counting how many times a Zone has been raided
 */



/**
 * A zombie game mostly made of options and menus. Sending survivors scavenging, etc.
 * This class does not feature display and should be instancied or extended for real use.
 * @author LogicalKip
 *
 */
public class BitingDeathGame {

				/* ATTRIBUTES */
	/**
	 * Number of rations (1 survivor needs 1 ration a day)
	 */
	protected int rations;
	
	/**
	 * Alive survivors in the team
	 */
	protected List<Survivor> survivors;
	
	/**
	 * Day 0, Day 1, ... since the beginning of the game
	 */
	protected int currentDay;
	
	/**
	 * The raid as it will be run for the current day.
	 */
	protected RaidSettings currentRaidSettings;
	
	/**
	 * If the game is lost.
	 */
	protected boolean gameOver;
	
	/**
	 * Ex : "All survivors are dead". Should be displayed when the game is lost.
	 */
	protected String gameOverMessage;
	
	/**
	 * The messages that must be displayed to the player. Ex : "X died during a mission", "X has gone because no food left", etc...
	 * They must be displayed in fifo order, and then removed from the list.
	 */
	protected List<String> messagesToDisplay;
	
	/**
	 * The map of the game, made of different Zones
	 */
	protected Map map;
	
	protected Rules rules;
	
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
		this.currentRaidSettings = null;
	}

	//TODO prevent same-name survivors from existing
	/**
	 * Returns a new list of N random survivors, N being defined in this.rules
	 */
	protected List<Survivor> getRandomSurvivorList() {
		List<Survivor> survivors = new LinkedList<Survivor>();
		for (int i = 0 ; i < this.rules.getNbSurvivorsStart() ; i++)
			survivors.add(new Survivor());
		return survivors;
	}
	
	/**
	 * When the player ends the current day.
	 * @throws CantRunRaidException 
	 */
	public void nextDay() throws CantRunRaidException {
		if (this.currentRaidSettings != null)
			this.runRaid(this.currentRaidSettings);
		
		this.map.randomZombieRoaming();
		this.rations -= this.survivors.size(); // Eating at evening
		
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
	 * Dead survivors will be removed from the list and added as zombies in the raided Zone.
	 * @param raidSettings Must already be created and initialized : team, etc.
	 * @throws CantRunRaidException 
	 */
	private void runRaid(RaidSettings raidSettings) throws CantRunRaidException {
		Raid raid = new Raid(raidSettings);
		
		
		raid.run();
		
		
		this.messagesToDisplay.addAll(raid.getMessagesToDisplayOnceRaidIsOver());
		LinkedList<Survivor> deadSurvivors = raid.getSurvivorsHurtDuringRaid();
		
		try {
			raid.getDestination().addZombies(deadSurvivors.size());
		} catch (IncoherentNumberException e) {
			System.err.println("Erreur de code dans BitingDeathGame/runRaid : le nombre de zombies à ajouter est incohérent");
			e.printStackTrace();
		}
		
		Iterator<Survivor> iter = deadSurvivors.iterator();
		
		while (iter.hasNext()) { // Removing dead survivors from the game
			Survivor nextHurtSurvivor = iter.next();
			this.removeSurvivorFromGame(nextHurtSurvivor);
		}
		
		this.rations += raid.getLoot();
	}
	
	/**
	 * Add a message to a list, waiting to be displayed to the player. (fifo order)
	 * @param msg Does nothing if null
	 */
	public void addMessageToDisplay(String msg) {
		if (msg != null)
			this.messagesToDisplay.add(msg);
	}
	
	/**
	 * @return Return true if the fifo list of messages-to-display-to-the-user is not empty
	 */
	public boolean thereAreMessagesToDisplay() {
		return ! this.messagesToDisplay.isEmpty();
	}
	
	/**
	 * Remove the first message-to-display from the fifo list and return it.
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
		this.currentRaidSettings.team.remove(s);
	}
	
	//TODO LinkedList->List
	
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
	public LinkedList<Survivor> getSurvivors() {
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
	public RaidSettings getCurrentRaidSettings() {
		return this.currentRaidSettings;
	}


	/**
	 * @param currentRaidSettings the currentRaidSettings to set
	 */
	public void setCurrentRaidSettings(RaidSettings currentRaidSettings) {
		this.currentRaidSettings = currentRaidSettings;
	}

	public int getRations() {
		return rations;
	}	
}
