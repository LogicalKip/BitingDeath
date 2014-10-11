package com.logicalkip.bitingdeath.tests;

import java.util.LinkedList;

import com.logicalkip.bitingdeath.bitingdeath.Raid;
import com.logicalkip.bitingdeath.bitingdeath.mapping.ForestZone;
import com.logicalkip.bitingdeath.bitingdeath.rules.Rules;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;
import com.logicalkip.bitingdeath.exceptions.CantRunRaidException;

/**
 * Test class. All tests regarding the Raid class. Should not be used for testing other classes.
 * @author LogicalKip
 *
 */
public class RaidTests {

	public static void main(String[] args) {
		LinkedList<Survivor> survivors = new LinkedList<Survivor>();
		for (int i = 0 ; i < Rules.NB_SURVIVORS_START_DEFAULT ; i++)
			survivors.add(new Survivor());
			
		Survivor john = new Survivor("John \"Badass\" Walker");
		survivors.add(john);
		ForestZone z = new ForestZone("Sherwood Forest");
		
		System.out.println("Zombies at first : " + z.getZombiesLeft());
		
		Raid raid = new Raid(survivors, z);
		
		try {
			raid.run();
		} catch (CantRunRaidException e) {
			e.printStackTrace();
		}
		System.out.println("\nZombies left : " + z.getZombiesLeft() + "\n" + raid);
		try {
			raid.run();
		} catch (CantRunRaidException e) {
			e.printStackTrace();
		}
		System.out.println("\nZombies left : " + z.getZombiesLeft() + "\n" + raid);
		
	}

}
