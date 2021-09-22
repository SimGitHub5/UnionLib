package com.stereowalker.unionlib.item;

import java.util.ArrayList;
import java.util.List;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.item.AccessoryItem.AccessorySlotType;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class UItems {
	public static final List<Item> ITEMS = new ArrayList<Item>();

	public static final Item GOLDEN_RING = register("golden_ring", new AccessoryItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).durability(100), AccessorySlotType.RING));
			
	public static Item register(String name, Item item) {
		item.setRegistryName(UnionLib.location(name));
		ITEMS.add(item);
		return item;
	}
	
	public static void registerAll(IForgeRegistry<Item> registry) {
		for(Item item : ITEMS) {
			registry.register(item);
		}
	}
}
