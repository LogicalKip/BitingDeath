package com.logicalkip.bitingdeath.exceptions;

import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;


/**
 * Meant for problems occurring when a survivor equips a weapon
 * @author charles
 */
public class CantEquipWeaponException extends Exception {
	private static final long serialVersionUID = -1458756159521386147L;

	private String message;
	
	public CantEquipWeaponException(String msg) {
		this.message = msg;
	}
	
	public String getMessage() {
		return message;
	}
	
	/**
	 * Returns a message describing the error of a survivor not included in the player's group.
	 * @param s
	 */
	public static String notInTheTeam(Survivor s) {
		return "Survivor " + s.toString() + " is not in the team";
	}
}
