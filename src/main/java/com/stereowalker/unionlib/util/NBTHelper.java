package com.stereowalker.unionlib.util;

import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;

public class NBTHelper {
	
	/**
	 * creates a NBT list from the array of doubles passed to this function
	 */
	public static ListNBT newDoubleNBTList(double... numbers) {
		ListNBT listnbt = new ListNBT();

		for(double d0 : numbers) {
			listnbt.add(DoubleNBT.valueOf(d0));
		}

		return listnbt;
	}
	
	public class NbtType {
		public static final int IntNBT = 3;
		public static final int CompoundNBT = 10;
	}
}
