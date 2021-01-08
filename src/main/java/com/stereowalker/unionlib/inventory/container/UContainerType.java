package com.stereowalker.unionlib.inventory.container;

import java.util.ArrayList;
import java.util.List;

import com.stereowalker.unionlib.UnionLib;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.IForgeRegistry;

public class UContainerType {
	public static final List<ContainerType<?>> CONTAINERTYPES = new ArrayList<ContainerType<?>>();
	public static final ContainerType<UnionContainer> UNION = register("union_inventory", UnionContainer::new);

	public static <T extends Container> ContainerType<T> register(String name, ContainerType.IFactory<T> factory) {
		ContainerType<T> container = new ContainerType<>(factory);
		container.setRegistryName(UnionLib.location(name));
		CONTAINERTYPES.add(container);
		return container;
	}

	public static void registerAll(IForgeRegistry<ContainerType<?>> registry) {
		for(ContainerType<?> container : CONTAINERTYPES) {
			registry.register(container);
//			UnionLib.debug("Container: \""+container.getRegistryName().toString()+"\" registered");
		}
//		UnionLib.debug("All Containers Registered");
	}

}
