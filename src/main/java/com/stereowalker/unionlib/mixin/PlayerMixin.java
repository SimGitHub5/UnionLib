package com.stereowalker.unionlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.stereowalker.unionlib.entity.ai.UAttributes;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    private PlayerMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @ModifyVariable(
            method = "getDigSpeed(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)F",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/effect/MobEffectUtil;hasDigSpeed(Lnet/minecraft/world/entity/LivingEntity;)Z"
            ),
            index = 3
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