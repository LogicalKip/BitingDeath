package com.logicalkip.bitingdeath.tests;

import java.util.LinkedList;

import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;
import com.logicalkip.bitingdeath.exceptions.OutOfBoundsException;

/**
 * Test class. All tests regarding the BitingDeathGame class. Should not be used
 * for testing other classes.
 * 
 * @author LogicalKip
 * 
 */
public class BitingDeathGameTests {

	public static void getSurvivorsTest(TextualBitingDeathGame game) {
		LinkedList<Survivor> team = game.getSurvivors();
		System.out.println(team);
		System.out.println(game.getSurvivorListToString());

		game.removeFirstSurvivor();

		System.out.println(team);
		System.out.println(game.getSurvivorListToString());
	}

	public static void main(String[] args) throws OutOfBoundsException {
		TextualBitingDeathGame game = new TextualBitingDeathGame();

		

		System.out.println(game.getNextMessageToDisplay());

		System.out.println("\nSurvivors still alive : " + game.getSurvivorListToString());

		RaidSettings raid = null;

		Zone zone0 = null;

		try {
			zone0 = game.getMap().getZone(0, 0);
		} catch (OutOfBoundsException e2) {
			e2.printStackTrace();
		}


		raid = new RaidSettings(zone0, game.getSurvivors());

		System.out.println("\n\nRAID AT FIRST: " + raid + "\nAt first, "
				+ zone0.getZombiesLeft() + " zombies left\n");

		while (!game.getGameOver()) {
			System.out.println(game.getInventory());
			
			game.setCurrentRaidSettings(raid);
			System.out.println("\nRunning the raid...");

//			System.out.println("\n\nRAID : " + raid + "\nStill "
//					+ zone0.getZombiesLeft() + " zombies left\n");

			String msg = game.getNextMessageToDisplay();
			while (msg != null) {
				System.out.println(msg);
				msg = game.getNextMessageToDisplay();
			}
			System.out.println("\nSurvivors still alive : " + game.getSurvivors().size() + "\n");
			game.nextDay();
		}

		System.out.println(game.getGameOverMessage());

	}

}
