package com.stereowalker.unionlib.mod;

import java.util.HashMap;
import java.util.Map;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.mod.MinecraftMod.LoadType;
import com.stereowalker.unionlib.registries.RegisterObjects;
import com.stereowalker.unionlib.registries.UnionLibRegistry;

public class ModHandler {
	public static final Map<String, MinecraftMod> mods = new HashMap<>();
	private static boolean hasRegisteredModObjects = false;
	public static boolean isModRegistered(String mod) {
		return mods.containsKey(mod);
	}

	public static void registerMod(MinecraftMod mod) {
		mods.put(mod.getModid(), mod);
	}

	public static void setup() {
		if (!hasRegisteredModObjects) {
			boolean setupLoadLevel = false;
			for (MinecraftMod mod : ModHandler.mods.values()) {
				///////////////////////////////////
				if (!setupLoadLevel) {
					if (mod.getLoadType() == LoadType.BOTH) {
						UnionLib.loadLevel = LoadType.BOTH;
						setupLoadLevel = true;
					} else if (mod.getLoadType() == LoadType.CLIENT) {
						if (UnionLib.loadLevel == LoadType.SERVER) {
							UnionLib.loadLevel = LoadType.BOTH;
							setupLoadLevel = true;
						} else {
							UnionLib.loadLevel = LoadType.CLIENT;
						}
					} else if (mod.getLoadType() == LoadType.SERVER) {
						if (UnionLib.loadLevel == LoadType.CLIENT) {
							UnionLib.loadLevel = LoadType.BOTH;
							setupLoadLevel = true;
						} else {
							UnionLib.loadLevel = LoadType.SERVER;
						}
					}
				} else break;
				///////////////////////////////////
				for (Class<?> clas : mod.getRegistries()) {
					RegisterObjects.register(clas);
				}
				UnionLibRegistry.registerObjects();
			}
			hasRegisteredModObjects = true;
		}
	}
}