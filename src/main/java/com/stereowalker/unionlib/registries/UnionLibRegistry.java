package com.stereowalker.unionlib.registries;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.mod.MinecraftMod;
import com.stereowalker.unionlib.mod.MinecraftMod.LoadType;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;

public class UnionLibRegistry
{
	public static void registerObjects(MinecraftMod mod) {
		if (UnionLib.loadLevel != LoadType.CLIENT) {
			RegisterObjects.putObjectsInFabricRegistries(mod);

			Map<EntityType<? extends LivingEntity>, AttributeSupplier> oldMap = DefaultAttributes.SUPPLIERS;
			ImmutableMap.Builder<EntityType<? extends LivingEntity>, AttributeSupplier> newMap = ImmutableMap.builder();
			for (EntityType<? extends LivingEntity> entity : oldMap.keySet()) {
				AttributeSupplier.Builder build = AttributeSupplier.builder();
				oldMap.get(entity).instances.forEach((attr, inst) -> {
					build.add(attr, inst.getBaseValue());
				});
				if (mod.appendAttributesWithoutValues().get(entity) != null) mod.appendAttributesWithoutValues().get(entity).forEach((attr) -> build.add(attr));
				if (mod.appendAttributesWithValues().get(entity) != null) mod.appendAttributesWithValues().get(entity).forEach((attr) -> build.add(attr.getA(), attr.getB()));
				newMap.put(entity, build.build());
			}
			DefaultAttributes.SUPPLIERS = newMap.build();
		}
	}
}
