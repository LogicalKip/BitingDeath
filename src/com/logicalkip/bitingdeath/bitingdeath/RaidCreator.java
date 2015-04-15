/**
 * 
 */
package com.logicalkip.bitingdeath.bitingdeath;

import java.util.List;

import com.logicalkip.bitingdeath.bitingdeath.mapping.Base;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Map;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;

/**
 * The implementing class can create and add new raids
 * @author charles
 *
 */
public interface RaidCreator {
	/**
	 * Returns survivors that may be chosen by the user to run the soon-to-be-created raid.
	 */
	List<Survivor> getPickableRaiders();
	
	/**
	 * Returns the main base, so that it won't be displayed as a possible destination for the raid
	 */
	Base getMainBase();
	
	/**
	 * Returns the map of the game, so that the user may choose a destination for the raid
	 */
	Map getMap();
	
	/**
	 * Returns all zones that can't be chosen as a possible destination because there is already a team that will be sent there
	 */
	List<Zone> getZonesAlreadyBeingRaided();
	
	/**
	 * Use the newly created raid
	 * @param raid the newly created raid
	 */
	void addRaid(RaidSettings raid);
}