package com.stereowalker.unionlib.item;

import com.stereowalker.unionlib.item.AccessoryItem.AccessorySlotType;
import com.stereowalker.unionlib.registries.RegistryHolder;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

@RegistryHolder("unionlib")
public class UItems {
	@RegistryHolder("golden_ring")
	public static final Item GOLDEN_RING = new AccessoryItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).durability(100), AccessorySlotType.RING);
}
