package com.stereowalker.unionlib.supporter;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.stereowalker.unionlib.UnionLib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class CapeHandler {
	// Copied from SkinManager
	private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
	private static final Logger logger = LogManager.getLogger(UnionLib.MOD_ID);

	/**
	 * Queue the replacement of a player's cape with the Combat cape.
	 * <p>
	 * In at least 100 milliseconds, the player's cape will be replaced on the next iteration of the client's main loop.
	 *
	 * @param player The player
	 */
	public static void queuePlayerCapeReplacement(final AbstractClientPlayer player) {
		if (doesPlayerNeedCapeClient(player)) {
			final String displayName = player.getDisplayName().getString();


			if (willPlayerShowCape(player))
				THREAD_POOL.submit(() -> {
					logger.info("Queueing cape replacement for " + displayName);
					try {
						Thread.sleep(100);
					} catch (final InterruptedException e) {
						logger.fatal("Cape delay thread for "+displayName+" interrupted");
						return;
					}

					Minecraft.getInstance().submitAsync(() -> replacePlayerCape(player));
				});
			else 
				logger.info(displayName+" has decided to not display their custom cape");
		}
	}

	/**
	 * Replace a player's cape with the TestMod3 cape.
	 *
	 * @param player The player
	 */
	private static void replacePlayerCape(final AbstractClientPlayer player) {
		final String displayName = player.getDisplayName().getString();
		final PlayerInfo playerInfo = player.getPlayerInfo();
		if (playerInfo == null) {
			logger.info("PlayerInfo of "+displayName+" is null. Cannot add cape");
			return;
		}
		final Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = playerInfo.textureLocations;
		playerTextures.put(MinecraftProfileTexture.Type.CAPE, new ResourceLocation(UnionLib.MOD_ID, "textures/cape/"+Supporters.CAPES_LOCATION.get(player.getUUID())+".png"));
		logger.info("Looking for cape texture at " + new ResourceLocation(UnionLib.MOD_ID, "textures/cape/"+Supporters.CAPES_LOCATION.get(player.getUUID())+".png").getPath());
		logger.info("Replaced cape of " + displayName);
	}

	/**
	 * Does the player have a Custom cape?
	 * <p>
	 * Currently only returns true for Stereowalker, Dev, CodeHexIO and other Patrons
	 *
	 * @param player The player
	 * @return True if the player has a Custom cape
	 */
	public static boolean doesPlayerNeedCapeClient(final AbstractClientPlayer player) {
		return Supporters.CAPES_LOCATION.containsKey(player.getUUID());
	}

	/**
	 * Does the player have a Custom cape?
	 * <p>
	 * Currently only returns true for Stereowalker, Dev, CodeHexIO and other Patrons
	 *
	 * @param player The player
	 * @return True if the player has a Custom cape
	 */
	public static boolean willPlayerShowCape(final AbstractClientPlayer player) {
		return Supporters.CAPES_SHOWN.containsKey(player.getUUID()) && Supporters.CAPES_SHOWN.get(player.getUUID());
	}
}
