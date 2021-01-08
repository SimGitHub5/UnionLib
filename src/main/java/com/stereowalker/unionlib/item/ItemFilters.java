package com.stereowalker.unionlib.item;

import java.util.function.Predicate;

import com.stereowalker.unionlib.item.AccessoryItem.AccessorySlotType;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFilters {
	public static final Predicate<ItemStack> RINGS = (stack) -> {
		return stack.getItem() instanceof AccessoryItem && ((AccessoryItem)stack.getItem()).accessoryType == AccessorySlotType.RING;
	};
	
	public static final Predicate<ItemStack> NECKLACES = (stack) -> {
		return stack.getItem() instanceof Item;
	};
}
