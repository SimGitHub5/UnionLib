package com.stereowalker.unionlib.util;

import java.util.List;

import com.stereowalker.unionlib.config.UnionValues;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RegistryHelper {
	public static boolean matchesRegisteredEntry(ResourceLocation name, ForgeRegistryEntry<?> registry) {
		return registry.getRegistryName().equals(name);
	}
	
	public static boolean matchesRegisteredEntry(String name, ForgeRegistryEntry<?> registry) {
		ResourceLocation loc = new ResourceLocation(name);
		return matchesRegisteredEntry(loc, registry);
	}
	
	public static boolean listContainsRegisteredEntry(List<String> names, ForgeRegistryEntry<?> registry) {
		for (String name : names) {
			if (matchesRegisteredEntry(name, registry)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean listContainsRegisteredEntry(UnionValues.ConfigValue<List<String>> names, ForgeRegistryEntry<?> registry) {
		return listContainsRegisteredEntry(names.get(), registry);
	}
	
	public static <T extends Enum<?>> T rotateEnumForward(T input, T[] values) {
		if (input.ordinal() == values.length - 1) {
			return values[0];
		}
		else {
			return values[input.ordinal() + 1];
		}
	}
	
	public static <T extends Enum<?>> T rotateEnumBackward(T input, T[] values) {
		if (input.ordinal() == values.length - 1) {
			return values[0];
		}
		else {
			return values[input.ordinal() + 1];
		}
	}
	
}
