package com.stereowalker.unionlib.item;

import java.util.function.Predicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class AccessoryItem extends Item {
	AccessorySlotType accessoryType;

	public AccessoryItem(Properties properties, AccessorySlotType type) {
		super(properties);
		this.accessoryType = type;
	}

	//	@Override
	//	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
	//		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	//		if (itemSlot == 9) {
	//			if (entityIn instanceof LivingEntity)
	//			this.accessoryTick((LivingEntity) entityIn, stack);
	//		}
	//	}

	//	/**
	//	 * Confirms if this accessory is currently equipped by {@link player}
	//	 */
	//	public boolean isEquipped(PlayerEntity player) {
	//		if (accessoryType.getValidStack().test(new ItemStack(this))) {
	//			if (Combat.isCuriosLoaded) {
	//				return CuriosApi.getCuriosHelper().findEquippedCurio(this, player).map(ringIn -> {return accessoryType.getValidStack().test(ringIn.getRight());}).orElse(false);
	//			} else {
	//				return accessoryType.getValidStack().test(player.inventory.getStackInSlot(9));
	//			}
	//		}
	//		return false;
	//	}

	//	/**
	//	 * Gets an ItemStack instance of the equipped accessory. Returns null when no the accessory is not equipped {@link player}
	//	 */
	//	public ItemStack getEquippedRing(PlayerEntity player) {
	//		if (isEquipped(player)) {
	//			if (Combat.isCuriosLoaded) {
	//				return CuriosApi.getCuriosHelper().findEquippedCurio(accessoryType.getValidStack(), player).map(ringIn -> {return ringIn.getRight();}).orElse(ItemStack.EMPTY);
	//			} else {
	//				return player.inventory.getStackInSlot(9);
	//			}
	//		}
	//		return ItemStack.EMPTY;
	//	}

	public void accessoryTick(World world, LivingEntity entity, ItemStack stack, int slot) {
		
	}

	public ITextComponent accessoryInformation() {
		return null;
	}
	
	public AccessorySlotType getAccessoryType() {
		return accessoryType;
	}

	public enum AccessorySlotType {
		NECKLACE(ItemFilters.NECKLACES, 0),
		RING(ItemFilters.RINGS, 1);

		Predicate<ItemStack> validStack;
		int index;

		private AccessorySlotType(Predicate<ItemStack> validStackIn, int index) {
			this.validStack = validStackIn;
			this.index = index;
		}

		public Predicate<ItemStack> getValidStack() {
			return validStack;
		}

		public int getIndex() {
			return this.index;
		}
	}

}
