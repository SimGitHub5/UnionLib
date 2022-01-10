package com.stereowalker.unionlib.registries;

import java.lang.reflect.Field;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.mod.MinecraftMod;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class RegisterObjects {
	public static void putObjectsInFabricRegistries(MinecraftMod mod) {
		for (Class<?> classs : mod.getRegistries()) {
			UnionLib.debug("UnionLib Registry: Found class "+classs);
			RegistryHolder reg = null;
			boolean hasAnnotation = classs.isAnnotationPresent(RegistryHolder.class);
			if (hasAnnotation)reg = classs.getAnnotation(RegistryHolder.class);
			for (Field field : classs.getFields()) {
				if (field.isAnnotationPresent(RegistryHolder.class)) {
					RegistryHolder regi = field.getAnnotation(RegistryHolder.class);
					ResourceLocation resourceName = new ResourceLocation(hasAnnotation ? reg.value()+":"+regi.value() : regi.value());
					try {
						if (field.get(null) != null) {
							if (field.get(null) instanceof Fluid) {
								Fluid fluid = (Fluid)field.get(null); Registry.register(Registry.FLUID, resourceName, fluid);
							}
							else if (field.get(null) instanceof Item) {
								Item item = (Item)field.get(null); Registry.register(Registry.ITEM, resourceName, item);
							}
							else if (field.get(null) instanceof MenuType) {
								MenuType<?> menu = (MenuType<?>)field.get(null); Registry.register(Registry.MENU, resourceName, menu);
							}
							else if (field.get(null) instanceof Attribute) {
								Attribute attribute = (Attribute)field.get(null); Registry.register(Registry.ATTRIBUTE, resourceName, attribute);
							}
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
