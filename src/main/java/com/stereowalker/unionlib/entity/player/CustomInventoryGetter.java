package com.stereowalker.unionlib.entity.player;

import com.stereowalker.unionlib.inventory.UnionInventory;

public interface CustomInventoryGetter {
	
	public abstract UnionInventory getUnionInventory();
	public abstract void setUnionInventory(UnionInventory inv);

}
