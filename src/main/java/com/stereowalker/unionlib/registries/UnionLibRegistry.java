package com.stereowalker.unionlib.registries;

import java.util.function.Consumer;

import com.google.common.collect.Maps;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.mod.MinecraftMod.LoadType;

import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class UnionLibRegistry
{
	public static void registerObjects() {
		if (UnionLib.loadLevel != LoadType.CLIENT) {
			RegisterObjects.putObjectsInForgeRegistries();
			FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<EntityAttributeModificationEvent>)event-> {
				UnionLib.mods.forEach((mod) -> Maps.newHashMap(mod.appendAttributesWithoutValues()).forEach((ent, ma) -> ma.forEach((attr) -> event.add(ent, attr))));
				UnionLib.mods.forEach((mod) -> Maps.newHashMap(mod.appendAttributesWithValues()).forEach((ent, ma) -> ma.forEach((attr) -> event.add(ent, attr.getA(), attr.getB()))));
			});
		}
	}
}
