package com.logicalkip.bitingdeath.bitingdeath;

import java.util.LinkedList;

import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;

/**
 * @author LogicalKip
 * Showing WHO the player decided will go WHERE 
 * Could possibily be saved and used to run several Raid (as a "favourite")
 */
public class RaidSettings {
	/**
	 * The place the team will raid. Might be a building, a part of a forest, of a city...
	 */
	protected Zone destination;
	
	/**
	 * Survivors running the mission
	 */
	protected LinkedList<Survivor> team;
	
	
	public RaidSettings(Zone destination, LinkedList<Survivor> team) {
		this.destination = destination;
		
		
		if (team == null)
			this.team = new LinkedList<Survivor>();
		else
			this.team = team;
	}
	
	/* 				GETTERS/SETTERS			*/

	/**
	 * @return the destination
	 */
	public Zone getDestination() {
		return this.destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(Zone destination) {
		this.destination = destination;
	}
	

	/**
	 * @return the team
	 */
	public LinkedList<Survivor> getTeam() {
		return this.team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(LinkedList<Survivor> team) {
		this.team = team;
	}
	
	
}
