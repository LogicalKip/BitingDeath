package com.logicalkip.bitingdeath.bitingdeath;

import java.util.Iterator;
import java.util.LinkedList;

import com.logicalkip.bitingdeath.bitingdeath.mapping.Map;
import com.logicalkip.bitingdeath.bitingdeath.rules.Rules;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;
import com.logicalkip.bitingdeath.exceptions.CantRunRaidException;
import com.logicalkip.bitingdeath.exceptions.IncoherentNumberException;


/* 
 * TODO idea list for improving the game : 
 * being able to build stuff on zones (barricades, bridges in swamp, towers, etc) 
 * XP on survivor's skills : fight, scavenge,... (100 max)
 * find a way to be sure that a dying survivor is removed from all lists/var, even a forgotten or not so useful one (leader of survivors, last team used, raids, ...)
 * 		NOTES :
 * 			faire ma classe survlist qui h�rite de list<surv>, qui poss�de en attribut static les survlist ainsi cr��es
 * 			(via new), et une m�thode static qui supprime les surv de toutes les survlist existantes. 
 * 			pb : et les variables simples ? et si on renew une liste (dans une autre classe), l'ancienne sera toujours
 * 			dans survlist.attributstatic et continuera d'etre updat�e ? 
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
	protected LinkedList<Survivor> survivors;
	
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
	protected LinkedList<String> messagesToDisplay;
	
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

	
	/**
	 * Returns a new list of N random survivors, N being defined in this.rules
	 */
	protected LinkedList<Survivor> getRandomSurvivorList() {
		LinkedList<Survivor> survivors = new LinkedList<Survivor>();
		for (int i = 0 ; i < this.rules.getNbSurvivorsStart() ; i++)
			survivors.add(new Survivor());
		return survivors;
	}
	
	/**
	 * When the player ends the current day.
	 */
	public void nextDay() {
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
	 * @param raidSettings Must already be created and initialised : team, etc.
	 */
	private void runRaid(RaidSettings raidSettings) {
		Raid raid = new Raid(raidSettings);
		
		try {
			raid.run();
		} catch (CantRunRaidException e1) {
			e1.printStackTrace();
		}
		
		this.messagesToDisplay.addAll(raid.getMessagesToDisplayOnceRaidIsOver());
		LinkedList<Survivor> deadSurvivors = raid.getSurvivorsHurtDuringRaid();
		
		try {
			raid.getDestination().addZombies(deadSurvivors.size());
		} catch (IncoherentNumberException e) {
			System.err.println("Erreur de code dans BitingDeathGame/runRaid : le nombre de zombies � ajouter est incoh�rent");
			e.printStackTrace();
		}
		
		Iterator<Survivor> iter = deadSurvivors.iterator();
		
		while (iter.hasNext()) { // Removing dead survivors from the game list and the raid list
			Survivor nextHurtSurvivor = iter.next();
			this.survivors.remove(nextHurtSurvivor); //TODO change when found a way to update all lists as s1 dies. 
			raid.removeSurvivor(nextHurtSurvivor);
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
	 * Return true if the fifo list of messages-to-display-to-the-user is not empty
	 * @return
	 */
	public boolean thereAreMessagesToDisplay() {
		return ! this.messagesToDisplay.isEmpty();
	}
	
	/**
	 * Remove the first message-to-display from the fifo list and return it.
	 * @return the first message of the current list of messages to display, or null if the list is empty
	 */
	public String getNextMessageToDisplay() {
		if (this.thereAreMessagesToDisplay())
		{
			String nextMsg = this.messagesToDisplay.getFirst();
			this.messagesToDisplay.removeFirst();
			return nextMsg;
		}
		else
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
}