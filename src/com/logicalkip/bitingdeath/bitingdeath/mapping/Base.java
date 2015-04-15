package com.logicalkip.bitingdeath.bitingdeath.mapping;

import java.util.LinkedList;
import java.util.List;

import com.logicalkip.bitingdeath.bitingdeath.Crowbar;
import com.logicalkip.bitingdeath.bitingdeath.Screwdriver;
import com.logicalkip.bitingdeath.bitingdeath.Weapon;

/**
 * A base where survivors are taking shelter and storing resources
 * @author charles
 *
 */
public class Base {
	/**
	 * Abscissa on the map (0 <= x <= N-1)
	 */
	private int x;
	

	/**
	 * Ordinate on the map (0 <= y <= N-1)
	 */
	private int y;

	private List<Weapon> availableWeapons;
	
	public Base(int x, int y) {
		this.x = x;
		this.y = y;
		this.availableWeapons = this.getInitialWeapons();
	}
	
	private List<Weapon> getInitialWeapons() {
		List<Weapon> res = new LinkedList<Weapon>();
		res.add(new Crowbar());
		res.add(new Screwdriver());
		res.add(new Crowbar());
		
		return res;
	}

	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the availableWeapons
	 */
	public List<Weapon> getAvailableWeapons() {
		return availableWeapons;
	}
}
