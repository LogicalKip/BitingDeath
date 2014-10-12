package com.logicalkip.bitingdeath.tests;

import java.util.ListIterator;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;

/**
 * Isn't meant to actually be played. Used for testing purpose, providing display of data.
 * @author LogicalKip
 *
 */
public class TextualBitingDeathGame extends BitingDeathGame {
	
	/**
	 * CONSTRUCTOR
	 */
	public TextualBitingDeathGame() {
		super();
	}
	
	/**
	 * returns the survivors as text
	 * @return toStrings methode concatened
	 */
	public String getSurvivorListToString() {
		String res = "";
		ListIterator<Survivor> i = this.survivors.listIterator();
		while (i.hasNext())
			res += i.next().toString() + "\n";
		return res;
	}
	
	/**
	 * Return a String describing the current inventory (rations, etc) in the game
	 */
	public String getInventory() {
		return "There are currently " + this.rations + " rations left.";
	}
	
	public void removeFirstSurvivor() {
		this.survivors.remove(0);
	}
	
}
