package com.stereowalker.unionlib.util;

import net.minecraftforge.fml.ModList;

public class ModHelper {

	public static boolean isBOPLoaded(boolean shouldReturnTrue) {
		if (!shouldReturnTrue)return ModList.get().isLoaded("biomesoplenty");
		else return true;
	}
	public static boolean isJEILoaded() {
		return ModList.get().isLoaded("jei");
	}
	public static boolean isCuriosLoaded() {
		return ModList.get().isLoaded("curios");
	}
	public static boolean isMantleLoaded() {
		return ModList.get().isLoaded("mantle");
	}
	public static boolean isSereneSeasonsLoaded() {
		return ModList.get().isLoaded("sereneseasons");
	}
	public static boolean isPrimalWinterLoaded() {
		return ModList.get().isLoaded("primalwinter");
	}
	public static boolean isSurviveLoaded() {
		return ModList.get().isLoaded("survive");
	}
}
