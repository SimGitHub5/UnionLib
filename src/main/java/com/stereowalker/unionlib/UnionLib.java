package com.stereowalker.unionlib;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.stereowalker.unionlib.client.gui.screen.ConfigScreen;
import com.stereowalker.unionlib.client.gui.screen.inventory.UScreens;
import com.stereowalker.unionlib.client.keybindings.KeyBindings;
import com.stereowalker.unionlib.config.Config;
import com.stereowalker.unionlib.config.ConfigBuilder;
import com.stereowalker.unionlib.entity.ai.UAttributes;
import com.stereowalker.unionlib.inventory.UnionInventory;
import com.stereowalker.unionlib.mod.UnionMod;
import com.stereowalker.unionlib.network.PacketRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(value = "unionlib")
public class UnionLib {
//Komorebi

	public static UnionLib instance;
	public static final String MOD_ID = "unionlib";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final String INVENTORY_KEY = "UnionInventory";
	private static final String NETWORK_PROTOCOL_VERSION = "1";
	public static boolean debugMode = false;
	public static List<UnionMod> mods = new ArrayList<UnionMod>();
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(location("main"), () -> NETWORK_PROTOCOL_VERSION, NETWORK_PROTOCOL_VERSION::equals, NETWORK_PROTOCOL_VERSION::equals);
	
	public static void debug(String message) {
		if (debugMode) {
			UnionLib.LOGGER.debug(message);
		}
	}
	
	public static void warn(String message) {
		if (debugMode) {
			UnionLib.LOGGER.warn(message);
		}
	}
	
	public static boolean disableConfig() {
		return false;
	}
	
	public static boolean drawMainMenuButton() {
		return true;
	}

	public UnionLib() 
	{
		instance = this;
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ConfigBuilder.registerConfig(Config.class);
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientSetup);
		MinecraftForge.EVENT_BUS.register(this);
		UAttributes.registerAll(modEventBus);
		PacketRegistry.registerMessages(CHANNEL);
		
		for (int i = 0; i < 20; i++) {
			new UnionMod("concept"+i, location("name"), com.stereowalker.unionlib.mod.UnionMod.LoadType.BOTH, !FMLEnvironment.production) {
				@Override
				public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
					return new ConfigScreen(previousScreen, Config.class, new TranslationTextComponent("Config"));
				}
			};
		}
//		UnionMod con = new UnionMod("controllersupport", location("name"), LoadType.CLIENT);
//		UnionMod sur = new UnionMod("survive", location("name"), LoadType.BOTH);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		KeyBindings.registerKeyBindings();
		UScreens.registerScreens();
	}

	public static ResourceLocation location(String name)
	{
		return new ResourceLocation(MOD_ID, name);
	}


	/**
	 * Returns the players accessory inventory. If this is ever edited, save it with {@link UnionLib#saveInventory(PlayerEntity, UnionInventory)}
	 * @param player
	 * @return
	 */
	public static UnionInventory getAccessoryInventory (PlayerEntity player) {
		UnionInventory inventory = new UnionInventory(player);
		inventory.read(player.getPersistentData().getList(INVENTORY_KEY, 10));
		return inventory;
	}

	/**
	 * Saves the accessory inventory to the players NBT. If this is not called after changes have been made, it will not save
	 * @param player
	 * @param inventory
	 */
	public static void saveInventory(PlayerEntity player, UnionInventory inventory) {
		player.getPersistentData().put(INVENTORY_KEY, inventory.write());
	}
}
