package com.stereowalker.unionlib.util;

import java.util.function.Consumer;

import com.stereowalker.unionlib.UnionLib;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.AttributeModifierMap.MutableAttribute;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Stereowalker
 *
 */
public class EntityHelper {

	/**
	 *should from {@link PlayerEntity}. Added because it doesn't allow to specify which level
	 * @param level
	 * @return
	 */
	public static int getXpBarCap(int level) {
		if (level >= 30) {
			return 112 + (level - 30) * 9;
		} else {
			return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
		}
	}

	/**
	 *should from {@link PlayerEntity}. Added because {@link PlayerEntity#experienceLevel} doesn't return the actual value when just the players levels are deducted
	 * @param player
	 * @return The actual total experience of the player
	 */
	public static int getActualExperienceTotal(PlayerEntity player) {
		int xpTot = 0;
		for (int i = 0; i < player.experienceLevel; i++) {
			xpTot += getXpBarCap(i);
		}
		int f = MathHelper.floor(player.experience * (float)player.xpBarCap());
		xpTot+=f;
		return xpTot;
	}

	/**
	 *should from {@link LivingEntity}. Added because the AT cannot transform this
	 * @param level
	 * @return
	 */
	public static float getJumpUpwardsMotion(LivingEntity living) {
		return 0.42F * getJumpFactor(living);
	}

	protected static float getJumpFactor(Entity entity) {
		float f = entity.world.getBlockState(entity.getPosition()).getBlock().getJumpFactor();
		float f1 = entity.world.getBlockState(getPositionUnderneath(entity)).getBlock().getJumpFactor();
		return (double)f == 1.0D ? f1 : f;
	}

	protected static BlockPos getPositionUnderneath(Entity entity) {
		return new BlockPos(entity.getPosX(), entity.getBoundingBox().minY - 0.5000001D, entity.getPosZ());
	}

//	/**
//	 * Adds attributes to an entity type
//	 * @param type     Entity type
//	 * @param builder  Consumer for builder to add attributes
//	 */
//	@SuppressWarnings("deprecation")
//	public static void registerAttributes(EntityType<? extends LivingEntity> type, Consumer<MutableAttribute> builder) {
//		AttributeModifierMap.MutableAttribute newAttrs = AttributeModifierMap.createMutableAttribute();
//		if (GlobalEntityTypeAttributes.doesEntityHaveAttributes(type)) {
//			newAttrs.attributeMap.putAll(GlobalEntityTypeAttributes.getAttributesForEntity(type).attributeMap);
//		}
//		builder.accept(newAttrs);
//		GlobalEntityTypeAttributes.put(type, newAttrs.create());
//	}
}
