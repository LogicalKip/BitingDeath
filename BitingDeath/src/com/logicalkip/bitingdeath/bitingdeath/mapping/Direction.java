package com.logicalkip.bitingdeath.bitingdeath.mapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author LogicalKip
 * Cardinal directions : north, east, south, west (as for now...)
 */
public enum Direction {
	NORTH,
	EAST,
	SOUTH,
	WEST;
	
	private static final List<Direction> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();
	
	public static Direction randomDiretion()  {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}
