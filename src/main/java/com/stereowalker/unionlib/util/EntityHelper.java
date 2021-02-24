package com.stereowalker.unionlib.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
		float f = entity.world.getBlockState(entity.getPosition()).getBlock().func_226892_n_();
		float f1 = entity.world.getBlockState(getPositionUnderneath(entity)).getBlock().func_226892_n_();
		return (double)f == 1.0D ? f1 : f;
	}

	protected static BlockPos getPositionUnderneath(Entity entity) {
		return new BlockPos(entity.func_226277_ct_(), entity.getBoundingBox().minY - 0.5000001D, entity.func_226281_cx_());
	}
}
