package com.logicalkip.bitingdeath.bitingdeath.mapping;

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

	public Base(int x, int y) {
		this.x= x;
		this.y = y;
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
}
