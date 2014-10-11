package com.logicalkip.bitingdeath.tests;

import com.logicalkip.bitingdeath.bitingdeath.mapping.KindOfZone;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Map;
import com.logicalkip.bitingdeath.bitingdeath.mapping.PlainZone;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.exceptions.OutOfBoundsException;

/**
 * Class used for testing Map, Zone, extended Zones, and any class fairly related to Map.
 * Should not be used for testing other classes.
 * @author LogicalKip
 *
 */
public class MappingTests {
	public static void extendZoneTest(Map map, int x, int y) {
		System.out.println("Plains of the map : ");
		for (int i = 0 ; i < x ; i++)
		{
			for (int j = 0 ; j < y ; j++)
				try {
					if (map.getZone(i, j).getKindOfZone() == KindOfZone.PLAIN)
					{
						PlainZone plain = (PlainZone) map.getZone(i, j);
						System.out.println(plain.getName() + " : " + plain.getZombiesLeft() + " zombies left");
					}
						
				} catch (OutOfBoundsException e) {					
					e.printStackTrace();
				}
		}
	}
	
	public static void displayZonesOfMap(Map map, int x, int y) {
		for (int i = 0 ; i < x ; i++)
		{
			for (int j = 0 ; j < y ; j++)
				try {
					System.out.println(map.getZone(i, j).getName() + " : " + map.getZone(i, j).getZombiesLeft() + " zombies left");
				} catch (OutOfBoundsException e) {					
					e.printStackTrace();
				}
			
			System.out.println("");
		}
		
	}
	
	/**
	 * Avoiding the annoying try/catch
	 * @param x
	 * @param y
	 * @param map
	 * @return Hopefully, the zone. If null, check x and y
	 */
	public static Zone getZone(int x, int y, Map map) {
		try {
			return map.getZone(x, y);
		} catch (OutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void displayZombiePopulation(Map map, int x, int y) {
		for (int j = 0 ; j < y ; j++)
		{
			for (int i = 0 ; i < x ; i++)
				System.out.print(getZone(i, j, map).getZombiesLeft() + " ");
			
			System.out.println("");
		}
	}

	public static void zombieRoamingTest(Map map, int x, int y) {
		displayZombiePopulation(map, x, y);
		
		System.out.println("\n\nZombies are walking...\n\n");
		map.randomZombieRoaming();
		System.out.println("Zombies may have moved\n\n");
		
		displayZombiePopulation(map, x, y);
	}
	public static void main(String[] args) {
		int x = 4, y = 3; // x is the width of map, and y is height
		Map map = new Map(x, y);
		zombieRoamingTest(map, x, y);
	}

}
