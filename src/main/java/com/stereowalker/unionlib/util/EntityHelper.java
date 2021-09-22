package com.stereowalker.unionlib.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * @author Stereowalker
 *
 */
public class EntityHelper {

	/**
	 *should from {@link Player}. Added because it doesn't allow to specify which level
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
	 *should from {@link Player}. Added because {@link Player#experienceLevel} doesn't return the actual value when just the players levels are deducted
	 * @param player
	 * @return The actual total experience of the player
	 */
	public static int getActualExperienceTotal(Player player) {
		int xpTot = 0;
		for (int i = 0; i < player.experienceLevel; i++) {
			xpTot += getXpBarCap(i);
		}
		int f = Mth.floor(player.experienceProgress * (float)player.getXpNeededForNextLevel());
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
		float f = entity.level.getBlockState(entity.blockPosition()).getBlock().getJumpFactor();
		float f1 = entity.level.getBlockState(getPositionUnderneath(entity)).getBlock().getJumpFactor();
		return (double)f == 1.0D ? f1 : f;
	}

	protected static BlockPos getPositionUnderneath(Entity entity) {
		return new BlockPos(entity.getX(), entity.getBoundingBox().minY - 0.5000001D, entity.getZ());
	}
}
