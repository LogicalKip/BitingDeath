package com.logicalkip.bitingdeath.bitingdeath.rules;

/**
 * @author LogicalKip
 * Numbers ands others things ruling the map, whether changed by user or still default.
 */
public class MapRules {
	
	/**
	 * Default number of Zones lines in the map
	 */
	public static final int MAP_HEIGHT_DEFAULT = 5;
	
	/**
	 * Default number of Zones columns in the map
	 */
	public static final int MAP_WIDTH_DEFAULT = 5;
	
	
	/**
	 * Default number of zombies in a ForestZone
	 */
	public static final int FOREST_NB_ZOMBIES_DEFAULT = 5;
	
	/**
	 * Default number of zombies in a PlainZone
	 */
	public static final int PLAIN_NB_ZOMBIES_DEFAULT = 3;
	
	/**
	 * Number of lines in the map
	 */
	protected int mapHeight;
	
	/**
	 * Number of columns in the map
	 */
	protected int mapWidth;
	
	/**
	 * Approximate number of zombies in a forest when starting the game
	 */
	protected int forestNbZombies;
	
	/**
	 * Approximate number of zombies in a plain when starting the game
	 */
	protected int plainNbZombies;
	
				/* METHODS */
	
	/**
	 * CONSTRUCTOR
	 */
	public MapRules() {
		this.mapHeight = MapRules.MAP_HEIGHT_DEFAULT;
		this.mapWidth = MapRules.MAP_WIDTH_DEFAULT;
		this.forestNbZombies = MapRules.FOREST_NB_ZOMBIES_DEFAULT;
		this.plainNbZombies = MapRules.PLAIN_NB_ZOMBIES_DEFAULT;
	}

	/**
	 * @return the mapHeight
	 */
	public int getMapHeight() {
		return this.mapHeight;
	}

	/**
	 * @param mapHeight the mapHeight to set
	 */
	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}
	

	/**
	 * @return the mapWidth
	 */
	public int getMapWidth() {
		return this.mapWidth;
	}

	/**
	 * @param mapWidth the mapWidth to set
	 */
	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}
	

	/**
	 * @return the forestNbZombies
	 */
	public int getForestNbZombies() {
		return this.forestNbZombies;
	}

	/**
	 * @param forestNbZombies the forestNbZombies to set
	 */
	public void setForestNbZombies(int forestNbZombies) {
		this.forestNbZombies = forestNbZombies;
	}
	

	/**
	 * @return the plainNbZombies
	 */
	public int getPlainNbZombies() {
		return this.plainNbZombies;
	}

	/**
	 * @param plainNbZombies the plainNbZombies to set
	 */
	public void setPlainNbZombies(int plainNbZombies) {
		this.plainNbZombies = plainNbZombies;
	}
	
	
}
