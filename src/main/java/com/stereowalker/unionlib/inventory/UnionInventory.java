package com.stereowalker.unionlib.inventory;

import com.stereowalker.unionlib.item.AccessoryItem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class UnionInventory extends SimpleContainer {
	public final Player player;
	public UnionInventory(Player player) {
		super(9);
		this.player = player;
	}

	//	   public void openInventory(Player player) {
	//	      if (this.associatedChest != null) {
	//	         this.associatedChest.openChest();
	//	      }
	//
	//	      super.openInventory(player);
	//	   }
	//
	//	   public void closeInventory(Player player) {
	//	      if (this.associatedChest != null) {
	//	         this.associatedChest.closeChest();
	//	      }
	//
	//	      super.closeInventory(player);
	//	      this.associatedChest = null;
	//	   }

	public void read(ListTag p_70486_1_) {
		for(int i = 0; i < this.getContainerSize(); ++i) {
			this.setItem(i, ItemStack.EMPTY);
		}

		for(int k = 0; k < p_70486_1_.size(); ++k) {
			CompoundTag compoundnbt = p_70486_1_.getCompound(k);
			int j = compoundnbt.getByte("Slot") & 255;
			if (j >= 0 && j < this.getContainerSize()) {
				this.setItem(j, ItemStack.of(compoundnbt));
			}
		}

	}

	public ListTag write() {
		ListTag listnbt = new ListTag();

		for(int i = 0; i < this.getContainerSize(); ++i) {
			ItemStack itemstack = this.getItem(i);
			if (!itemstack.isEmpty()) {
				CompoundTag compoundnbt = new CompoundTag();
				compoundnbt.putByte("Slot", (byte)i);
				itemstack.save(compoundnbt);
				listnbt.add(compoundnbt);
			}
		}

		return listnbt;
	}

	/**
	 * Decrement the number of animations remaining. Only called on client side. This is used to handle the animation of
	 * receiving a block.
	 */
	public void tick() {
		for (int i = 0; i < this.getContainerSize(); i++) {
			if (!this.getItem(i).isEmpty()) {
				if (this.getItem(i).getItem() instanceof AccessoryItem) {
					((AccessoryItem)this.getItem(i).getItem()).accessoryTick(player.level, player, this.getItem(i), i);
				}
			}
		}
	}

	/**
	 * Drop all armor and main inventory items.
	 */
	public void dropAllItems() {
		for(int i = 0; i < this.getContainerSize(); ++i) {
			ItemStack itemstack = this.getItem(i);
			if (!itemstack.isEmpty()) {
				this.player.drop(itemstack, true, false);
				this.setItem(i, ItemStack.EMPTY);
			}
		}

	}

	/**
	 * Copy the ItemStack contents from another InventoryPlayer instance
	 */
	public void copyInventory(UnionInventory playerInventory) {
		for(int i = 0; i < this.getContainerSize(); ++i) {
			this.setItem(i, playerInventory.getItem(i));
		}
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	public boolean isUsableByPlayer(Player player) {
		return true;
	}
	
	public ItemStack getFirstRing() {
		return this.getItem(3);
	}
	
	public ItemStack getSecondRing() {
		return this.getItem(6);
	}
}
