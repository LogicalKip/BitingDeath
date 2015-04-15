package com.logicalkip.bitingdeath.bitingdeath;

public class Screwdriver extends Weapon {
	public Screwdriver() {
	}

	@Override
	public String getName() {
		return "Screwdriver";
	}

	@Override
	public double getEfficiency() {
		return 0.4;
	}
}
