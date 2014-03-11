package com.logicalkip.bitingdeath.exceptions;

/**
 * @author LogicalKip
 * The game doesn't allow the raid to be run, for some reason. Check it and try again !
 */
public class CantRunRaidException extends Exception {
	
	private static final long serialVersionUID = 3485209554142966780L;
	
	/**
	 * Explaining why it can't be run 
	 */
	protected CantRunRaidBecause reason;
	
	public CantRunRaidException(CantRunRaidBecause reason) {
		this.reason = reason;
	}

	/**
	 * @return the reason
	 */
	public CantRunRaidBecause getReason() {
		return this.reason;
	}
}
