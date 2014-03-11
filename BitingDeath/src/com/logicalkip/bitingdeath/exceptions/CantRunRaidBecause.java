package com.logicalkip.bitingdeath.exceptions;

/**
 * @author LogicalKip
 * Given to constructor of CantRunRaidException, to explain why it can't be run 
 * (e.g : no survivors have been selected to run the raid)
 */
public enum CantRunRaidBecause {
	/**
	 * No survivors were chosen for raiding
	 */
	NO_SURVIVORS,
	
	/**
	 * The chosen destination is null.
	 */
	DESTINATION_NULL;
	
}
