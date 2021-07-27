package com.stereowalker.unionlib;

import java.io.BufferedReader;
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

	//CLient
	private static final Map<UUID, String> CAPES_LOCATION = Maps.newHashMap();
	
	public static void loadCapes() {
		try {
	         URL url = new URL("https://raw.githubusercontent.com/Stereowalker/UnionLib/1.16-forge/capes.json");
	         URLConnection connection = url.openConnection();
	         connection.connect();
	         System.out.println("Found the cape Json file on GitHub");
	         
	         BufferedReader read = new BufferedReader(
	         new InputStreamReader(url.openStream()));
	         
	         JsonObject object = parser.parse(read).getAsJsonObject();
	         
	         for (Entry<String, JsonElement> element: object.entrySet()) {
	        	 System.out.println("Found cape for "+element.getKey()+" they seem to want to use the "+element.getValue().getAsString()+" cape");
	        	 CAPES_LOCATION.put(UUID.fromString(element.getKey()), element.getValue().getAsString());
	         }
	         read.close();
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
		playerTextures.put(MinecraftProfileTexture.Type.CAPE, new ResourceLocation(UnionLib.MOD_ID, "textures/cape/"+CAPES_LOCATION.get(PlayerEntity.getUUID(player.getGameProfile()))+".png"));
		logger.info("Looking for cape texture at " + new ResourceLocation(UnionLib.MOD_ID, "textures/cape/"+CAPES_LOCATION.get(PlayerEntity.getUUID(player.getGameProfile()))+".png").getPath());
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
	public static boolean doesPlayerNeedCapeClient(final AbstractClientPlayerEntity player) {
		return CAPES_LOCATION.containsKey(PlayerEntity.getUUID(player.getGameProfile()));
	}
}
