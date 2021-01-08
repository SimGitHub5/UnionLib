package com.stereowalker.unionlib.inventory;

import com.stereowalker.unionlib.item.AccessoryItem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class UnionInventory extends Inventory {
	public final PlayerEntity player;
	public UnionInventory(PlayerEntity player) {
		super(9);
		this.player = player;
	}

	//	   public void openInventory(PlayerEntity player) {
	//	      if (this.associatedChest != null) {
	//	         this.associatedChest.openChest();
	//	      }
	//
	//	      super.openInventory(player);
	//	   }
	//
	//	   public void closeInventory(PlayerEntity player) {
	//	      if (this.associatedChest != null) {
	//	         this.associatedChest.closeChest();
	//	      }
	//
	//	      super.closeInventory(player);
	//	      this.associatedChest = null;
	//	   }

	public void read(ListNBT p_70486_1_) {
		for(int i = 0; i < this.getSizeInventory(); ++i) {
			this.setInventorySlotContents(i, ItemStack.EMPTY);
		}

		for(int k = 0; k < p_70486_1_.size(); ++k) {
			CompoundNBT compoundnbt = p_70486_1_.getCompound(k);
			int j = compoundnbt.getByte("Slot") & 255;
			if (j >= 0 && j < this.getSizeInventory()) {
				this.setInventorySlotContents(j, ItemStack.read(compoundnbt));
			}
		}

	}

	public ListNBT write() {
		ListNBT listnbt = new ListNBT();

		for(int i = 0; i < this.getSizeInventory(); ++i) {
			ItemStack itemstack = this.getStackInSlot(i);
			if (!itemstack.isEmpty()) {
				CompoundNBT compoundnbt = new CompoundNBT();
				compoundnbt.putByte("Slot", (byte)i);
				itemstack.write(compoundnbt);
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
		for (int i = 0; i < this.getSizeInventory(); i++) {
			if (!this.getStackInSlot(i).isEmpty()) {
				if (this.getStackInSlot(i).getItem() instanceof AccessoryItem) {
					((AccessoryItem)this.getStackInSlot(i).getItem()).accessoryTick(player.world, player, this.getStackInSlot(i), i);
				}
			}
		}
	}

	/**
	 * Drop all armor and main inventory items.
	 */
	public void dropAllItems() {
		for(int i = 0; i < this.getSizeInventory(); ++i) {
			ItemStack itemstack = this.getStackInSlot(i);
			if (!itemstack.isEmpty()) {
				this.player.dropItem(itemstack, true, false);
				this.setInventorySlotContents(i, ItemStack.EMPTY);
			}
		}

	}

	/**
	 * Copy the ItemStack contents from another InventoryPlayer instance
	 */
	public void copyInventory(UnionInventory playerInventory) {
		for(int i = 0; i < this.getSizeInventory(); ++i) {
			this.setInventorySlotContents(i, playerInventory.getStackInSlot(i));
		}
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}
	
	public ItemStack getFirstRing() {
		return this.getStackInSlot(3);
	}
	
	public ItemStack getSecondRing() {
		return this.getStackInSlot(6);
	}
}
