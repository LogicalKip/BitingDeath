
package com.logicalkip.bitingdeath.bitingdeath.mapping;

import com.logicalkip.bitingdeath.exceptions.IncoherentNumberException;

/**
 * The whole Map is divided vertically and horizontally in Zones, which can be of different kinds :
 * Forest, Building, Swamp, etc. They have different features and characteristics. One can run a Raid into a Zone
 * @author LogicalKip
 *
 */
public abstract class Zone {
				
				/* ATTRIBUTES */
	/**
	 * Ex : "Darkwood Forest - North", "Nowhere City" :)
	 */
	protected String name;// TODO random names (prefix-suffix system)
	
	/**
	 * Init depends on the kind of Zone (more in towns, etc)
	 */
	protected int zombiesLeft;
	
	protected KindOfZone kindOfZone;
	
	
				/* METHODS */
	/**
	 * CONSTRUCTOR
	 * @param name The full name of the Zone, maybe including a subname. 
	 * if null, a random name is chosen
	 */
	public Zone(String name, KindOfZone kindOfZone) {
		this.zombiesLeft = zombiesAtStart();
		if (name == null)
			this.name = this.getRandomName();
		else
			this.name = name;
		this.kindOfZone = kindOfZone;
	}
	
	/**
	 * Return the number of zombie in this Zone when the game starts.
	 * Must be redefined by the kind of Zone (ex : more zombies in towns)
	 */
	protected abstract int zombiesAtStart();
	
	/**
	 * Return the name the Zone could be named after.
	 * Depends on the kind of Zone ("Forest of X", ...)
	 * @return
	 */
	protected abstract String getRandomName();

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the zombies left
	 */
	public int getZombiesLeft() {
		return this.zombiesLeft;
	}

	/**
	 * @param zombiesToRemove Zombies have been killed or leaving, therefore removing them from the total number
	 * @throws IncoherentNumberException if zombiesToRemove < 0 or if there aren't that many zombies to remove
	 */
	public synchronized void removeZombies(int zombiesToRemove) throws IncoherentNumberException {
		if (zombiesToRemove < 0)
			throw new IncoherentNumberException();
		else
			this.adjustZombieNumber(- zombiesToRemove);
	}
	
	/**
	 * Adds zombies to the total number of the Zone
	 * @param zombiesToAdd Some more zombies roaming the place
	 * @throws IncoherentNumberException if zombiesToAdd < 0
	 */
	public synchronized void addZombies(int zombiesToAdd) throws IncoherentNumberException {
		if (zombiesToAdd < 0)
			throw new IncoherentNumberException();
		else
			this.adjustZombieNumber(+ zombiesToAdd);
	}
	
	
	/**
	 * Adds zombiesToAdd to the total number of zombies in the Zone
	 * Thus, a negative number will remove some.
	 * @param zombiesToAdd Some more (or less, for example if they were killed) zombies roaming the place
	 * @throws IncoherentNumberException if zombiesToAdd < 0 but there aren't that many zombies to remove.
	 */
	public synchronized void adjustZombieNumber(int zombiesToAdd) throws IncoherentNumberException {
		if (zombiesToAdd < 0 && (this.zombiesLeft + zombiesToAdd) < 0) // Ex : 5 zombiesLeft and -7 zombiesToAdd
			throw new IncoherentNumberException();
		else
			this.zombiesLeft += zombiesToAdd;
	}

	/**
	 * @param name the name to set
	 * Does nothing if null or ""
	 */
	public void setName(String name) {
		if (name != null && ! name.equals(""))
			this.name = name;		
	}
	
	/**
	 * @return the kind of zone (Forest, plain, etc), so that one can know inwhat subclass one should cast.
	 */
	public KindOfZone getKindOfZone() {
		return this.kindOfZone;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	

	
}
