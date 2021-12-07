package com.stereowalker.unionlib.registries;

import java.lang.reflect.Field;
import java.util.List;

import com.google.common.collect.Lists;
import com.stereowalker.unionlib.UnionLib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterObjects {
	static final List<Class<?>> registries = Lists.newArrayList();

	public static void register(Class<?> re) {
		registries.add(re);
	}

	public static void putItemsInForgeRegistries(IForgeRegistry<Item> forgeRegistry) {
		for (Class<?> classs : registries) {
			UnionLib.debug("UnionLib Registry: Found class "+classs);
			RegistryHolder reg = null;
			boolean hasAnnotation = classs.isAnnotationPresent(RegistryHolder.class);
			if (hasAnnotation)reg = classs.getAnnotation(RegistryHolder.class);
			for (Field field : classs.getFields()) {
				if (field.isAnnotationPresent(RegistryHolder.class)) {
					RegistryHolder regi = field.getAnnotation(RegistryHolder.class);
					try {
						if (field.get(null) != null && field.get(null) instanceof Item) {
							Item item = (Item)field.get(null);
							if (hasAnnotation)
								item.setRegistryName(new ResourceLocation(reg.value()+":"+regi.value()));
							else
								item.setRegistryName(new ResourceLocation(regi.value()));
							forgeRegistry.register(item);
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
