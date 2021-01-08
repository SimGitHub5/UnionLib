package com.stereowalker.unionlib.util.math;

import java.util.Random;

public class UnionMathHelper {
	/**
	 * Do a probability check. If 
	 * @param probability - If this value is 0 or greater than 1000, it will return false
	 * @return
	 */
	public static boolean probabilityCheck(int probability) {
		Random ran = new Random();
		if (probability > 0 && probability <= 1000) {
			int random = ran.nextInt(1000);
			return random < probability;
		}
		return false;
	}

	public static double roundDecimal(int i, double value) {
		int modInt = (int) (value*(Math.pow(i, 10)));
		return modInt / (Math.pow(i, 10));
	}
}
