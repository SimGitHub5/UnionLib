package com.stereowalker.unionlib.registries;

import java.lang.reflect.Field;
import java.util.List;

import com.google.common.collect.Lists;
import com.stereowalker.unionlib.UnionLib;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;

public class RegisterObjects {
	static final List<Class<?>> registries = Lists.newArrayList();

	public static void register(Class<?> re) {
		registries.add(re);
	}

	public static void putObjectsInFabricRegistries() {
		for (Class<?> classs : registries) {
			UnionLib.debug("UnionLib Registry: Found class "+classs);
			RegistryHolder reg = null;
			boolean hasAnnotation = classs.isAnnotationPresent(RegistryHolder.class);
			if (hasAnnotation)reg = classs.getAnnotation(RegistryHolder.class);
			for (Field field : classs.getFields()) {
				if (field.isAnnotationPresent(RegistryHolder.class)) {
					RegistryHolder regi = field.getAnnotation(RegistryHolder.class);
					try {
						if (field.get(null) != null) {
							if (field.get(null) instanceof Item) {
								Item item = (Item)field.get(null);
								Registry.register(Registry.ITEM, new ResourceLocation(hasAnnotation ? reg.value()+":"+regi.value() : regi.value()), item);
							}
							else if (field.get(null) instanceof MenuType) {
								MenuType<?> menu = (MenuType<?>)field.get(null);
								Registry.register(Registry.MENU, new ResourceLocation(hasAnnotation ? reg.value()+":"+regi.value() : regi.value()), menu);
							}
							else if (field.get(null) instanceof Attribute) {
								Attribute attribute = (Attribute)field.get(null);
								Registry.register(Registry.ATTRIBUTE, new ResourceLocation(hasAnnotation ? reg.value()+":"+regi.value() : regi.value()), attribute);
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
