package com.stereowalker.unionlib.inventory.container;

import com.stereowalker.unionlib.registries.RegistryHolder;

import net.minecraft.world.inventory.MenuType;

@RegistryHolder("unionlib")
public class UContainerType {
	@RegistryHolder("union_inventory")
	public static final MenuType<UnionContainer> UNION = new MenuType<UnionContainer>(UnionContainer::new);
}
