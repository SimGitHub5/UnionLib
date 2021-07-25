package com.stereowalker.unionlib;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class Cape {
	// Copied from SkinManager
	private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
	private static final Logger logger = LogManager.getLogger(UnionLib.MOD_ID);
	private static final JsonParser parser = new JsonParser();

	private static final Map<UUID, Integer> CAPES = Maps.newHashMap();
	
	public static void loadCapes() {
		try {
	         URL url = new URL("http://github.com/capes.json");
	         URLConnection connection = url.openConnection();
	         connection.connect();
	         System.out.println("Internet is connected");
	         
	         JsonObject object = parser.parse(new InputStreamReader(url.openStream())).getAsJsonObject();
	         
	         for (Entry<String, JsonElement> element: object.entrySet()) {
	        	 CAPES.put(UUID.fromString(element.getKey()), element.getValue().getAsInt());
	         }
	      } catch (MalformedURLException e) {
	         System.out.println("Internet is not connected");
	      } catch (IOException e) {
	         System.out.println("Internet is not connected");
	      }
	}
	/**
	 * Queue the replacement of a player's cape with the Combat cape.
	 * <p>
	 * In at least 100 milliseconds, the player's cape will be replaced on the next iteration of the client's main loop.
	 *
	 * @param player The player
	 */
	public static void queuePlayerCapeReplacement(final AbstractClientPlayerEntity player) {
		final String displayName = player.getDisplayName().getString();

		logger.info("Queueing cape replacement for " + displayName);

		THREAD_POOL.submit(() -> {
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				logger.fatal("Cape delay thread for "+displayName+" interrupted");
				return;
			}

			Minecraft.getInstance().deferTask(() -> replacePlayerCape(player));
		});
	}

	/**
	 * Replace a player's cape with the TestMod3 cape.
	 *
	 * @param player The player
	 */
	private static void replacePlayerCape(final AbstractClientPlayerEntity player) {
		final String displayName = player.getDisplayName().getString();
		final NetworkPlayerInfo playerInfo = player.getPlayerInfo();
		if (playerInfo == null) {
			logger.info("NetworkPlayerInfo of "+displayName+" is null. Cannot add cape");
			return;
		}
		final Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = playerInfo.playerTextures;
		playerTextures.put(MinecraftProfileTexture.Type.CAPE, new ResourceLocation(UnionLib.MOD_ID, "textures/cape/cape"+capeTexture(player)+".png"));
		logger.info("Looking for cape texture at " + new ResourceLocation(UnionLib.MOD_ID, "textures/cape/cape"+capeTexture(player)+".png").getPath());
		logger.info("Replaced cape of " + displayName);
	}

	/**
	 * Does the player have a C.O.M.B.A.T. cape?
	 * <p>
	 * Currently only returns true for Stereowalker, Dev, CodeHexIO and other Patrons
	 *
	 * @param player The player
	 * @return True if the player has a C.O.M.B.A.T. cape
	 */
	public static boolean doesPlayerNeedCape(final AbstractClientPlayerEntity player) {
		return capeTexture(player) != 0;
	}
	
	public static int capeTexture(final AbstractClientPlayerEntity player) {
		if (PlayerEntity.getUUID(player.getGameProfile()).equals(UUID.fromString("4da67eb9-7966-4aa3-b2f6-7ee90bf606d2"))) {
			return 10;//Stereowalker
		}
		if (PlayerEntity.getUUID(player.getGameProfile()).equals(UUID.fromString("b4640bf0-c41d-3c36-b901-fd2d90b54431"))) {
			return 9;//COdeHexIO
		}
		if (PlayerEntity.getUUID(player.getGameProfile()).equals(UUID.fromString("986e2557-0d63-37ec-acd8-20f952a01923"))) {
			return 3;//Jumper
		}
		if (PlayerEntity.getUUID(player.getGameProfile()).equals(UUID.fromString("e2401a9b-d035-3821-b275-03f8b4339b02"))) {
			return 7;//Angel
		}
		if (PlayerEntity.getUUID(player.getGameProfile()).equals(UUID.fromString("74649178-0bc8-3661-8503-68ca5190d4bb"))) {
			return 8;//Mimi
		}
		if (player.getDisplayName().getString().equals("Dev")) {
			return 1;
		}
		else return 0;
	}

}
