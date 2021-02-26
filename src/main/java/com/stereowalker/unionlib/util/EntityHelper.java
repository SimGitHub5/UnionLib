package com.stereowalker.unionlib.util;

import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.stereowalker.unionlib.event.BaseGameEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

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

	public static void registerAttributes(EntityType<? extends LivingEntity> type, Consumer<List<Pair<IAttribute,Double>>> builder) {
//		List<Pair<IAttribute,Double>> build = Lists.newArrayList();
//		builder.accept(build);
//		if (!BaseGameEvents.REGISTERED_ATTRIBUTES_1.containsKey(type)) {
//			BaseGameEvents.REGISTERED_ATTRIBUTES_1.put(type, Lists.newArrayList());
//		}
//		
//		List<Pair<IAttribute,Double>> newAttributeList = Lists.newArrayList();
//		for (Pair<IAttribute,Double> builderAttributes : build) {
//			if (BaseGameEvents.REGISTERED_ATTRIBUTES_1.get(type).size() > 0) {
//				for (Pair<IAttribute,Double> registeredAttributes : BaseGameEvents.REGISTERED_ATTRIBUTES_1.get(type)) {
//					if (builderAttributes.getFirst() == registeredAttributes.getFirst()) {
//						registeredAttributes = builderAttributes;//.setBaseValue(builderAttributes.getBaseValue());
//					} else {
//						newAttributeList.add(builderAttributes);
//					}
//				}
//			}
//		}
//		BaseGameEvents.REGISTERED_ATTRIBUTES_1.get(type).addAll(newAttributeList);
	}
}
