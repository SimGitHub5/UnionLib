package com.stereowalker.unionlib.inventory.container;

import java.util.ArrayList;
import java.util.List;

import com.stereowalker.unionlib.UnionLib;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.IForgeRegistry;

public class UContainerType {
	public static final List<MenuType<?>> CONTAINERTYPES = new ArrayList<MenuType<?>>();
	public static final MenuType<UnionContainer> UNION = register("union_inventory", UnionContainer::new);

	public static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType.MenuSupplier<T> factory) {
		MenuType<T> container = new MenuType<>(factory);
		container.setRegistryName(UnionLib.location(name));
		CONTAINERTYPES.add(container);
		return container;
	}

	public static void registerAll(IForgeRegistry<MenuType<?>> registry) {
		for(MenuType<?> container : CONTAINERTYPES) {
			registry.register(container);
//			UnionLib.debug("AbstractContainerMenu: \""+container.getRegistryName().toString()+"\" registered");
		}
//		UnionLib.debug("All Containers Registered");
	}

}
