package com.logicalkip.bitingdeath.bitingdeath;

import java.util.LinkedList;
import java.util.List;

import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;

/**
 * Showing WHOM the player decided will go WHERE.
 * Could possibly be saved and used to run several Raid (as a "favorite")
 * @author LogicalKip
 */
public class RaidSettings {
	/**
	 * The place the team will raid. Might be a building, a part of a forest, of a city...
	 */
	protected Zone destination;
	
	/**
	 * Survivors running the mission
	 */
	protected List<Survivor> team;
	
	
	public RaidSettings(Zone destination, List<Survivor> team) {
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
	public List<Survivor> getTeam() {
		return this.team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(LinkedList<Survivor> team) {
		this.team = team;
	}
	
	@Override
	public String toString() {
		String res = "";
		res += "Destination : " + this.destination.getName() + "\n";
		res += "Team : " + this.team.toString();
		return res;
	}
}
