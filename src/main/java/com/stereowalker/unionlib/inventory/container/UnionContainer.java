package com.stereowalker.unionlib.inventory.container;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.inventory.UnionInventory;
import com.stereowalker.unionlib.item.AccessoryItem.AccessorySlotType;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UnionContainer extends RecipeBookContainer<CraftingInventory> {
	public static final ResourceLocation LOCATION_BLOCKS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_SHIELD = new ResourceLocation("item/empty_armor_slot_shield");
	
	public static final ResourceLocation EMPTY_ACCESSORY_SLOT_NECKLACE = UnionLib.location("item/empty_accessory_slot_necklace");
	public static final ResourceLocation EMPTY_ACCESSORY_SLOT_RING = UnionLib.location("item/empty_accessory_slot_ring");
	private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
	private static final EquipmentSlotType[] VALID_EQUIPMENT_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
	private static final ResourceLocation[] ACCESSORY_SLOT_TEXTURES = new ResourceLocation[]{EMPTY_ACCESSORY_SLOT_NECKLACE, EMPTY_ACCESSORY_SLOT_RING};
	private static final AccessorySlotType[] VALID_ACCESSORY_SLOTS = new AccessorySlotType[]{AccessorySlotType.NECKLACE, AccessorySlotType.NECKLACE, AccessorySlotType.NECKLACE, AccessorySlotType.RING, AccessorySlotType.NECKLACE, AccessorySlotType.NECKLACE, AccessorySlotType.RING, AccessorySlotType.NECKLACE, AccessorySlotType.NECKLACE};
	private final CraftingInventory craftMatrix = new CraftingInventory(this, 2, 2);
	private final CraftResultInventory craftResult = new CraftResultInventory();
	private final UnionInventory inventory;
	public final boolean isLocalWorld;
	private final PlayerEntity player;

	public UnionContainer(int id, PlayerInventory playerInventory) {
		this(id, playerInventory, new UnionInventory(playerInventory.player), false, playerInventory.player);
	}

	public UnionContainer(int id, PlayerInventory playerInventory, UnionInventory unionInventory, boolean localWorld, PlayerEntity playerIn) {
		super(UContainerType.UNION, id);
		this.isLocalWorld = localWorld;
		this.player = playerIn;
		this.inventory = unionInventory;
		this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 143, 62));

		for(int i = 0; i < 2; ++i) {
			for(int j = 0; j < 2; ++j) {
				this.addSlot(new Slot(this.craftMatrix, j + i * 2, 134 + j * 18, 8 + i * 18));
			}
		}

		for(int k = 0; k < 4; ++k) {
			final EquipmentSlotType equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
			this.addSlot(new Slot(playerInventory, 39 - k, 8, 8 + k * 18) {
				/**
				 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in
				 * the case of armor slots)
				 */
				public int getSlotStackLimit() {
					return 1;
				}

				/**
				 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
				 */
				public boolean isItemValid(ItemStack stack) {
					return stack.canEquip(equipmentslottype, player);
				}

				/**
				 * Return whether this slot's stack can be taken from this slot.
				 */
				public boolean canTakeStack(PlayerEntity playerIn) {
					ItemStack itemstack = this.getStack();
					return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
				}

				@OnlyIn(Dist.CLIENT)
				@Override
				public Pair<ResourceLocation, ResourceLocation> func_225517_c_() {
					return Pair.of(UnionContainer.LOCATION_BLOCKS_TEXTURE, UnionContainer.ARMOR_SLOT_TEXTURES[equipmentslottype.getIndex()]);
				}
			});
		}

		for(int l = 0; l < 3; ++l) {
			for(int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new Slot(playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
			}
		}
		

		for(int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 142));
		}

		this.addSlot(new Slot(playerInventory, 40, 77, 62) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> func_225517_c_() {
				return Pair.of(UnionContainer.LOCATION_BLOCKS_TEXTURE, UnionContainer.EMPTY_ARMOR_SLOT_SHIELD);
			}
		});
		
		
		for(int l = 0; l < 3; ++l) {
			for(int j1 = 0; j1 < 3; ++j1) {
				int index = j1 + l * 3;
				final AccessorySlotType accessoryslottype = VALID_ACCESSORY_SLOTS[index];
				this.addSlot(new Slot(unionInventory, index, 77 + j1 * 18, 8 + l * 18) {
					/**
					 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in
					 * the case of armor slots)
					 */
					public int getSlotStackLimit() {
						return 1;
					}

					/**
					 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
					 */
					public boolean isItemValid(ItemStack stack) {
						return  accessoryslottype.getValidStack().test(stack);
					}

					/**
					 * Return whether this slot's stack can be taken from this slot.
					 */
					public boolean canTakeStack(PlayerEntity playerIn) {
						ItemStack itemstack = this.getStack();
						return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
					}

					@OnlyIn(Dist.CLIENT)
					@Override
					public Pair<ResourceLocation, ResourceLocation> func_225517_c_() {
						return Pair.of(UnionContainer.LOCATION_BLOCKS_TEXTURE, UnionContainer.ACCESSORY_SLOT_TEXTURES[accessoryslottype.getIndex()]);
					}
				});
			}
		}
	}

	public void fillStackedContents(RecipeItemHelper itemHelperIn) {
		this.craftMatrix.fillStackedContents(itemHelperIn);
	}

	public void clear() {
		this.craftResult.clear();
		this.craftMatrix.clear();
	}

	public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
		return recipeIn.matches(this.craftMatrix, this.player.world);
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		UnionLib.saveInventory(player, inventory);
		//TODO: Access Transform This later
//		      WorkbenchContainer.updateCraftingResult(this.windowId, this.player.world, this.player, this.craftMatrix, this.craftResult);
		updateCraftingResult(this.windowId, this.player.world, this.player, this.craftMatrix, this.craftResult);
	}
	protected static void updateCraftingResult(int id, World world, PlayerEntity player, CraftingInventory inventory, CraftResultInventory inventoryResult) {
	      if (!world.isRemote) {
	         ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
	         ItemStack itemstack = ItemStack.EMPTY;
	         Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, inventory, world);
	         if (optional.isPresent()) {
	            ICraftingRecipe icraftingrecipe = optional.get();
	            if (inventoryResult.canUseRecipe(world, serverplayerentity, icraftingrecipe)) {
	               itemstack = icraftingrecipe.getCraftingResult(inventory);
	            }
	         }

	         inventoryResult.setInventorySlotContents(0, itemstack);
	         serverplayerentity.connection.sendPacket(new SSetSlotPacket(id, 0, itemstack));
	      }
	   }

	/**
	 * Called when the container is closed.
	 */
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		this.craftResult.clear();
		if (!playerIn.world.isRemote) {
			this.clearContainer(playerIn, playerIn.world, this.craftMatrix);
		}
		UnionLib.saveInventory(playerIn, inventory);
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	public boolean canInteractWith(PlayerEntity playerIn) {
		UnionLib.saveInventory(player, inventory);
		return true;
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			EquipmentSlotType equipmentslottype = MobEntity.getSlotForItemStack(itemstack);
			if (index == 0) {
				if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 1 && index < 5) {
				if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 5 && index < 9) {
				if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
					return ItemStack.EMPTY;
				}
			} else if (equipmentslottype.getSlotType() == EquipmentSlotType.Group.ARMOR && !this.inventorySlots.get(8 - equipmentslottype.getIndex()).getHasStack()) {
				int i = 8 - equipmentslottype.getIndex();
				if (!this.mergeItemStack(itemstack1, i, i + 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (equipmentslottype == EquipmentSlotType.OFFHAND && !this.inventorySlots.get(45).getHasStack()) {
				if (!this.mergeItemStack(itemstack1, 45, 46, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 9 && index < 36) {
				if (!this.mergeItemStack(itemstack1, 36, 45, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 36 && index < 45) {
				if (!this.mergeItemStack(itemstack1, 9, 36, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
			if (index == 0) {
				playerIn.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}
	
//	@Override
//	public List<RecipeBookCategories> getRecipeBookCategories() {
//		return Lists.newArrayList(RecipeBookCategories.CRAFTING_SEARCH, RecipeBookCategories.CRAFTING_EQUIPMENT, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS, RecipeBookCategories.CRAFTING_MISC, RecipeBookCategories.CRAFTING_REDSTONE);
//	}

	/**
	 * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
	 * null for the initial slot that was double-clicked.
	 */
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
	}

	public int getOutputSlot() {
		return 0;
	}

	public int getWidth() {
		return this.craftMatrix.getWidth();
	}

	public int getHeight() {
		return this.craftMatrix.getHeight();
	}

	@OnlyIn(Dist.CLIENT)
	public int getSize() {
		return 5;
	}

	public CraftingInventory func_234641_j_() {
		return this.craftMatrix;
	}

	@Override
	//TODO Whut?
	public void func_201771_a(RecipeItemHelper p_201771_1_) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public RecipeBookCategory func_241850_m() {
//		return RecipeBookCategory.CRAFTING;
//	}
}