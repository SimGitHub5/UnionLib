package com.stereowalker.unionlib.inventory.container;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.inventory.UnionInventory;
import com.stereowalker.unionlib.item.AccessoryItem.AccessorySlotType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class UnionContainer extends RecipeBookMenu<CraftingContainer> {
	public static final ResourceLocation LOCATION_BLOCKS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_SHIELD = new ResourceLocation("item/empty_armor_slot_shield");
	
	public static final ResourceLocation EMPTY_ACCESSORY_SLOT_NECKLACE = UnionLib.instance.location("item/empty_accessory_slot_necklace");
	public static final ResourceLocation EMPTY_ACCESSORY_SLOT_RING = UnionLib.instance.location("item/empty_accessory_slot_ring");
	private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
	private static final EquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
	private static final ResourceLocation[] ACCESSORY_SLOT_TEXTURES = new ResourceLocation[]{EMPTY_ACCESSORY_SLOT_NECKLACE, EMPTY_ACCESSORY_SLOT_RING};
	private static final AccessorySlotType[] VALID_ACCESSORY_SLOTS = new AccessorySlotType[]{AccessorySlotType.NECKLACE, AccessorySlotType.NECKLACE, AccessorySlotType.NECKLACE, AccessorySlotType.RING, AccessorySlotType.NECKLACE, AccessorySlotType.NECKLACE, AccessorySlotType.RING, AccessorySlotType.NECKLACE, AccessorySlotType.NECKLACE};
	private final CraftingContainer craftMatrix = new CraftingContainer(this, 2, 2);
	private final ResultContainer craftResult = new ResultContainer();
	private final UnionInventory inventory;
	public final boolean isLocalWorld;
	private final Player player;

	public UnionContainer(int id, Inventory playerInventory) {
		this(id, playerInventory, new UnionInventory(playerInventory.player), false, playerInventory.player);
	}

	public UnionContainer(int id, Inventory playerInventory, UnionInventory unionInventory, boolean localWorld, Player playerIn) {
		super(UContainerType.UNION, id);
		this.isLocalWorld = localWorld;
		this.player = playerIn;
		this.inventory = unionInventory;
		this.addSlot(new ResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 143, 62));

		for(int i = 0; i < 2; ++i) {
			for(int j = 0; j < 2; ++j) {
				this.addSlot(new Slot(this.craftMatrix, j + i * 2, 134 + j * 18, 8 + i * 18));
			}
		}

		for(int k = 0; k < 4; ++k) {
			final EquipmentSlot equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
			this.addSlot(new Slot(playerInventory, 39 - k, 8, 8 + k * 18) {
				/**
				 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in
				 * the case of armor slots)
				 */
				@Override
				public int getMaxStackSize() {
					return 1;
				}

				/**
				 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
				 */
				@Override
				public boolean mayPlace(ItemStack stack) {
					return equipmentslottype == Mob.getEquipmentSlotForItem(stack);
				}

				/**
				 * Return whether this slot's stack can be taken from this slot.
				 */
				@Override
				public boolean mayPickup(Player playerIn) {
					ItemStack itemstack = this.getItem();
					return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.mayPickup(playerIn);
				}

				@Environment(EnvType.CLIENT)
				@Override
				public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
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
			@Environment(EnvType.CLIENT)
			public Pair<ResourceLocation, ResourceLocation> getBackground() {
				return Pair.of(UnionContainer.LOCATION_BLOCKS_TEXTURE, UnionContainer.EMPTY_ARMOR_SLOT_SHIELD);
			}
		});
		
		
		for(int l = 0; l < 3; ++l) {
			for(int j1 = 0; j1 < 3; ++j1) {
				int index = j1 + l * 3;
				final AccessorySlotType accessoryslottype = VALID_ACCESSORY_SLOTS[index];
				this.addSlot(new Slot(inventory, index, 77 + j1 * 18, 8 + l * 18) {
					/**
					 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in
					 * the case of armor slots)
					 */
					@Override
					public int getMaxStackSize() {
						return 1;
					}

					/**
					 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
					 */
					@Override
					public boolean mayPlace(ItemStack stack) {
						return  accessoryslottype.getValidStack().test(stack);
					}

					/**
					 * Return whether this slot's stack can be taken from this slot.
					 */
					@Override
					public boolean mayPickup(Player playerIn) {
						ItemStack itemstack = this.getItem();
						return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.mayPickup(playerIn);
					}

					@Environment(EnvType.CLIENT)
					@Override
					public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
						return Pair.of(UnionContainer.LOCATION_BLOCKS_TEXTURE, UnionContainer.ACCESSORY_SLOT_TEXTURES[accessoryslottype.getIndex()]);
					}
				});
			}
		}
	}

	@Override
	public void fillCraftSlotsStackedContents(StackedContents itemHelperIn) {
		this.craftMatrix.fillStackedContents(itemHelperIn);
	}

	@Override
	public void clearCraftingContent() {
		this.craftResult.clearContent();
		this.craftMatrix.clearContent();
	}

	@Override
	public boolean recipeMatches(Recipe<? super CraftingContainer> recipeIn) {
		return recipeIn.matches(this.craftMatrix, this.player.level);
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void slotsChanged(Container inventoryIn) {
//		UnionLib.saveInventory(player, inventory);
		//TODO: Access Transform This later
//		      WorkbenchContainer.updateCraftingResult(this.containerId, this.player.world, this.player, this.craftMatrix, this.craftResult);
		updateCraftingResult(this, this.player.level, this.player, this.craftMatrix, this.craftResult);
	}
	
	protected static void updateCraftingResult(UnionContainer container, Level world, Player player, CraftingContainer inventory, ResultContainer inventoryResult) {
	      if (!world.isClientSide) {
	         ServerPlayer serverplayerentity = (ServerPlayer)player;
	         ItemStack itemstack = ItemStack.EMPTY;
	         Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inventory, world);
	         if (optional.isPresent()) {
	            CraftingRecipe icraftingrecipe = optional.get();
	            if (inventoryResult.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
	               itemstack = icraftingrecipe.assemble(inventory);
	            }
	         }

	         inventoryResult.setItem(0, itemstack);
	         serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(container.containerId, container.incrementStateId(), 0, itemstack));
	      }
	   }

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
		this.craftResult.clearContent();
		if (!playerIn.level.isClientSide) {
			this.clearContainer(playerIn, this.craftMatrix);
		}
//		UnionLib.saveInventory(playerIn, inventory);
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean stillValid(Player playerIn) {
//		UnionLib.saveInventory(player, inventory);
		return true;
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			EquipmentSlot equipmentslottype = Mob.getEquipmentSlotForItem(itemstack);
			if (index == 0) {
				if (!this.moveItemStackTo(itemstack1, 9, 45, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index >= 1 && index < 5) {
				if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 5 && index < 9) {
				if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
					return ItemStack.EMPTY;
				}
			} else if (equipmentslottype.getType() == EquipmentSlot.Type.ARMOR && !this.slots.get(8 - equipmentslottype.getIndex()).hasItem()) {
				int i = 8 - equipmentslottype.getIndex();
				if (!this.moveItemStackTo(itemstack1, i, i + 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (equipmentslottype == EquipmentSlot.OFFHAND && !this.slots.get(45).hasItem()) {
				if (!this.moveItemStackTo(itemstack1, 45, 46, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 9 && index < 36) {
				if (!this.moveItemStackTo(itemstack1, 36, 45, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 36 && index < 45) {
				if (!this.moveItemStackTo(itemstack1, 9, 36, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
			if (index == 0) {
				playerIn.drop(itemstack1, false);
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
	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
		return slotIn.container != this.craftResult && super.canTakeItemForPickAll(stack, slotIn);
	}

	@Override
	public int getResultSlotIndex() {
		return 0;
	}

	@Override
	public int getGridWidth() {
		return this.craftMatrix.getWidth();
	}

	@Override
	public int getGridHeight() {
		return this.craftMatrix.getHeight();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public int getSize() {
		return 5;
	}

	public CraftingContainer getCraftSlots() {
		return this.craftMatrix;
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return RecipeBookType.CRAFTING;
	}

	@Override
	public boolean shouldMoveToInventory(int p_150591_) {
		return p_150591_ != this.getResultSlotIndex();
	}
}