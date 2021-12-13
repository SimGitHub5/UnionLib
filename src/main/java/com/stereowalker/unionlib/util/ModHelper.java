package com.stereowalker.unionlib.util;

import net.fabricmc.loader.api.FabricLoader;

public class ModHelper {

	public static boolean isBOPLoaded(boolean shouldReturnTrue) {
		if (!shouldReturnTrue)return FabricLoader.getInstance().isModLoaded("biomesoplenty");
		else return true;
	}
	public static boolean isJEILoaded() {
		return FabricLoader.getInstance().isModLoaded("jei");
	}
	public static boolean isCuriosLoaded() {
		return FabricLoader.getInstance().isModLoaded("curios");
	}
	public static boolean isMantleLoaded() {
		return FabricLoader.getInstance().isModLoaded("mantle");
	}
	public static boolean isSereneSeasonsLoaded() {
		return FabricLoader.getInstance().isModLoaded("sereneseasons");
	}
	public static boolean isPrimalWinterLoaded() {
		return FabricLoader.getInstance().isModLoaded("primalwinter");
	}
	public static boolean isSurviveLoaded() {
		return FabricLoader.getInstance().isModLoaded("survive");
	}
	public static boolean isOriginsLoaded() {
		return FabricLoader.getInstance().isModLoaded("origins");
	}
}
