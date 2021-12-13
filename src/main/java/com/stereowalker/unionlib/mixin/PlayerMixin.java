package com.stereowalker.unionlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.entity.ai.UAttributes;
import com.stereowalker.unionlib.entity.player.CustomInventoryGetter;
import com.stereowalker.unionlib.inventory.UnionInventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

/**
 * @author Stereowalker
 */
@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements CustomInventoryGetter {
	private UnionInventory uInv = new UnionInventory((Player)(Object)this);

	private PlayerMixin(EntityType<? extends LivingEntity> type, Level world) {
		super(type, world);
	}

	@Override
	public UnionInventory getUnionInventory() {
		return uInv;
	}

	@Override
	public void setUnionInventory(UnionInventory inv) {
		uInv = inv;
	}

	/**
	 * Called immediately after the players inventory ticks, used to tick the union inventory
	 */
	@Inject(method = "aiStep", at = @At("HEAD"))
	public void aiStep_inject(CallbackInfo ci){
		getUnionInventory().tick();
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveData_inject(CompoundTag pCompound, CallbackInfo ci) {
		pCompound.put(UnionLib.INVENTORY_KEY, this.uInv.save(new ListTag()));
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void readAdditionalSaveData_inject(CompoundTag pCompound, CallbackInfo ci) {
		ListTag listtag = pCompound.getList(UnionLib.INVENTORY_KEY, 10);
		this.uInv.load(listtag);
	}
	
	@Inject(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"))
	public void dropEquipment_inject(CallbackInfo ci) {
		uInv.dropAll();
	}
	
	@Inject(method = "destroyVanishingCursedItems", at = @At("TAIL"))
	public void destroyVanishingCursedItems_inject(CallbackInfo ci) {
		for(int i = 0; i < uInv.getContainerSize(); ++i) {
			ItemStack itemstack = uInv.getItem(i);
			if (!itemstack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemstack)) {
				uInv.removeItemNoUpdate(i);
			}
		}
	}

	@ModifyVariable(
			method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/effect/MobEffectUtil;hasDigSpeed(Lnet/minecraft/world/entity/LivingEntity;)Z"
					),
			index = 2
			)
	private float getDigSpeed(float f) {
		AttributeInstance instance = this.getAttribute(UAttributes.DIG_SPEED);

		if(instance != null) {
			for (AttributeModifier modifier : instance.getModifiers()) {
				float amount = (float) modifier.getAmount();

				if (modifier.getOperation() == AttributeModifier.Operation.ADDITION) {
					f += amount;
				} else {
					f *= (amount + 1);
				}
			}
		}

		return f;
	}
}