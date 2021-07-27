package com.stereowalker.unionlib;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.stereowalker.unionlib.network.client.play.SCapePacket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ServerCape {
	// Copied from SkinManager
	private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
	private static final Logger logger = LogManager.getLogger(UnionLib.MOD_ID);

	//Server
	public static final Map<UUID, Boolean> CAPES = Maps.newHashMap();
	
	public static void sendCapeDataToServer(UUID uuid, boolean val) {
		CAPES.put(uuid, val);
	}
	
	/**
	 * Queue the replacement of a player's cape with the Combat cape.
	 * <p>
	 * In at least 100 milliseconds, the player's cape will be replaced on the next iteration of the client's main loop.
	 *
	 * @param player The player
	 */
	public static void queuePlayerCapeReplacementServer(final ServerPlayerEntity player) {
		final String displayName = player.getDisplayName().getString();

		logger.info("Queueing cape replacement for " + displayName);

		THREAD_POOL.submit(() -> {
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				logger.fatal("Cape delay thread for "+displayName+" interrupted");
				return;
			}

			player.server.deferTask(() -> {
			if(ServerCape.doesPlayerNeedCape(player)) {
				new SCapePacket(/* ServerCape.CAPES */).send(player);
			}});
		});
	}

	/**
	 * Does the player have a cape and if so, do they want to display it?
	 * <p>
	 * Currently only returns true for Stereowalker, Dev, CodeHexIO and other Patrons
	 *
	 * @param player The player
	 * @return True if the player has a C.O.M.B.A.T. cape
	 */
	public static boolean doesPlayerNeedCape(final ServerPlayerEntity player) {
		System.out.println("Contains Key? "+CAPES.containsKey(PlayerEntity.getUUID(player.getGameProfile())));
		System.out.println("Enabled cape? "+CAPES.get(PlayerEntity.getUUID(player.getGameProfile())));
		return CAPES.containsKey(PlayerEntity.getUUID(player.getGameProfile())) && CAPES.get(PlayerEntity.getUUID(player.getGameProfile()));
	}
}
