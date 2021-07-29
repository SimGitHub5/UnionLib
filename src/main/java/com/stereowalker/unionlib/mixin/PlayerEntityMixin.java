package com.stereowalker.unionlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.stereowalker.unionlib.entity.ai.UAttributes;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    private PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @ModifyVariable(
            method = "getDigSpeed(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)F",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/potion/EffectUtils;hasMiningSpeedup(Lnet/minecraft/entity/LivingEntity;)Z"
            ),
            index = 3
    )
    private float getDigSpeed(float f) {
        ModifiableAttributeInstance instance = this.getAttribute(UAttributes.DIG_SPEED);

        if(instance != null) {
        	for (AttributeModifier modifier : instance.getModifierListCopy()) {
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