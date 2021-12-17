package com.stereowalker.unionlib.registries;

import java.util.function.Consumer;

import com.google.common.collect.Maps;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.mod.ModHandler;
import com.stereowalker.unionlib.mod.MinecraftMod;
import com.stereowalker.unionlib.mod.MinecraftMod.LoadType;

import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class UnionLibRegistry
{
	public static void registerObjects(MinecraftMod mod) {
//		ModHandler.mods.values().forEach((mod) -> {
			RegisterObjects.putObjectsInForgeRegistries(mod);
			FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<EntityAttributeModificationEvent>)event-> {
				Maps.newHashMap(mod.appendAttributesWithoutValues()).forEach((ent, ma) -> ma.forEach((attr) -> event.add(ent, attr)));
				Maps.newHashMap(mod.appendAttributesWithValues()).forEach((ent, ma) -> ma.forEach((attr) -> event.add(ent, attr.getA(), attr.getB())));
			});
//		});
	}
}
