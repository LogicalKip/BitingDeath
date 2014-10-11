
package com.logicalkip.bitingdeath.bitingdeath.mapping;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.exceptions.IncoherentNumberException;
import com.logicalkip.bitingdeath.exceptions.OutOfBoundsException;

/**
 * This represents where the game takes place. 
 * It might include towns, forests and such, represented as (one or several) Zones.
 * @author LogicalKip
 *
 */
public class Map {
	
	/**
	 * The Map is a rectangular table of Zones
	 */
	private Zone zones[][];	

	//TODO warfog (boolean[][])
	/**
	 * Number of columns
	 */
	private int width;
	
	
	/**
	 * Number of lines
	 */
	private int height;
	
	
	
	public Map(int width, int  height) {
		this.zones = new Zone[width][height];
		this.height = height;
		this.width = width;
		this.randomGeneration();
	}
	
	/**
	 * Randomly initialize this.zones
	 */
	private void randomGeneration() {
		//TODO sets of Zone. When creating them (e.g huge forest or town), add " - North"/etc to the name in the constructor
		double rand;
		for (int i = 0 ; i < this.width  ; i++)
		{
			for (int j = 0 ; j < this.height ; j++)
			{
				rand = BitingDeathGame.getRandomProbability();
				if (rand <= 0.5)
					this.zones[i][j] = new ForestZone();
				else
					this.zones[i][j] = new PlainZone();
			}
		}
	}
	
	/**
	 * Return true if, from the zone in [x,y], and going in dir, you are beyond the map (-1, etc) 
	 * [0, 0] is considered the zone high left.
	 * @param dir
	 * @param x
	 * @param y
	 */
	private boolean directionWillOutreachMapLimits (Direction dir, int x, int y) {
		int dx = 0, dy = 0;
		if (dir == Direction.NORTH)
			dy = -1;
		else if (dir == Direction.SOUTH)
			dy = 1;
		else if (dir == Direction.WEST)
			dx = -1;
		else if (dir == Direction.EAST)
			dx = 1;
		
		int newX = x + dx, newY = y + dy;
		boolean inBounds = (newX >= 0 && newX < this.width && newY >= 0 && newY < this.height);
		return !inBounds;
	}
	
	
	
	/**
	 * Make some of the zombies randomly walk into an adjacent zone of their previous location.
	 * Yeah, killing the zombies in a place doesn't mean there will never be anymore. Bitches walk.
	 */
	public void randomZombieRoaming() {
		
		/**
		 * If there is a negative number, that means more zombies left that zone than came in.
		 * Ex : 3 means there will be 3 more zombies in the zone, -5 means 5 less.
		 */
		int zombiesAddedAfterRoaming[][] = new int[this.width][this.height];
		
		// Initialization
		for (int x = 0 ; x < this.width ; x++)
			for (int y = 0 ; y < this.height ; y++)
				zombiesAddedAfterRoaming[x][y] = 0;
		
		// Simulating zombie exodus (zombies walk individually)
		for (int x = 0 ; x < this.width ; x++)
		{
			for (int y = 0 ; y < this.height ; y++)
			{
				for (int z = 0 ; z < this.zones[x][y].getZombiesLeft() ; z++) // for each zombie
				{
					if (BitingDeathGame.getRandomProbability() < 0.20) {
						// Moving elsewhere
		
						Direction randomDirection = Direction.randomDiretion();
						while (directionWillOutreachMapLimits(randomDirection, x, y))
							randomDirection = Direction.randomDiretion();
						
						// randomDirection is now a valid one
	
						if (randomDirection == Direction.NORTH)
							zombiesAddedAfterRoaming[x][y-1]++;
						else if (randomDirection == Direction.SOUTH)
							zombiesAddedAfterRoaming[x][y+1]++;
						else if (randomDirection == Direction.WEST)
							zombiesAddedAfterRoaming[x-1][y]++;
						else if (randomDirection == Direction.EAST)
							zombiesAddedAfterRoaming[x+1][y]++;
							
						zombiesAddedAfterRoaming[x][y]--; // Ciao !
					}
				}
			}
		}
		
		
		// Actualizing the map
		for (int x = 0 ; x < this.width ; x++)
			for (int y = 0 ; y < this.height ; y++)
				{
					try {
						this.zones[x][y].adjustZombieNumber(zombiesAddedAfterRoaming[x][y]);
					} 
					catch (IncoherentNumberException e) {
						System.out.println("Erreur de code dans Map/randomZombieRoaming : Nombre de zombies incoh�rent");
						e.printStackTrace();
					}
				}
	}
	
	/**
	 * @return the zones
	 */
	public Zone[][] getZones() {
		return this.zones;
	}
	
	/**
	 * @return a specific zone in the map
	 * @param x 0 <= x < width or else exception
	 * @param y 0 <= y < heigth or else exception
	 */
	public Zone getZone(int x, int y) throws OutOfBoundsException{
		if (x < 0 || x >= this.width || y < 0 || y >= this.height)
			throw new OutOfBoundsException();
		
		return this.zones[x][y];
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	

	/**
	 * @return the height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	
	
}
