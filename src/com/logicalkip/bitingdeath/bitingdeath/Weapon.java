package com.logicalkip.bitingdeath.bitingdeath;

/**
 * A weapon that can be stored in the base or used by a survivor.
 * @author charles
 *
 */
public abstract class Weapon {
	public abstract String getName();
	
	/**
	 * How great the weapon is at killing, including safety for user and damage.
	 * @return a double between 0 and 1, where 1 means "perfectly efficient"
	 */
	public abstract double getEfficiency();
}
