package com.stereowalker.unionlib;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.stereowalker.unionlib.client.gui.screen.ConfigScreen;
import com.stereowalker.unionlib.client.gui.screen.inventory.UScreens;
import com.stereowalker.unionlib.client.keybindings.KeyBindings;
import com.stereowalker.unionlib.config.TestClassConfig;
import com.stereowalker.unionlib.config.TestObjectConfig;
import com.stereowalker.unionlib.config.Config;
import com.stereowalker.unionlib.config.ConfigBuilder;
import com.stereowalker.unionlib.config.ServerConfig;
import com.stereowalker.unionlib.entity.ai.UAttributes;
import com.stereowalker.unionlib.inventory.UnionInventory;
import com.stereowalker.unionlib.item.UItems;
import com.stereowalker.unionlib.mod.UnionMod;
import com.stereowalker.unionlib.mod.UnionMod.LoadType;
import com.stereowalker.unionlib.network.PacketRegistry;
import com.stereowalker.unionlib.registries.RegisterObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

@Mod(value = "unionlib")
public class UnionLib {

	public static UnionLib instance;
	public static TestObjectConfig test_config = new TestObjectConfig();
	public static final Config CONFIG = new Config();
	public static final ServerConfig SERVER_CONFIG = new ServerConfig();
	public static final String MOD_ID = "unionlib";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final String INVENTORY_KEY = "UnionInventory";
	private static final String NETWORK_PROTOCOL_VERSION = "1";
	public static boolean debugMode = false;
	public static List<UnionMod> mods = new ArrayList<UnionMod>();
	public static LoadType loadLevel = null;
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

	public UnionLib() 
	{
		instance = this;
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ConfigBuilder.registerConfig(CONFIG);
		ConfigBuilder.registerConfig(SERVER_CONFIG);
		ConfigBuilder.registerConfig(TestClassConfig.class);
		ConfigBuilder.registerConfig(test_config);
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientSetup);
		MinecraftForge.EVENT_BUS.register(this);
		UAttributes.registerAll(modEventBus);
		PacketRegistry.registerMessages(CHANNEL);
		ClientCape.loadCapes();

		new UnionMod("unionlib", location("textures/gui/union_button.png"), com.stereowalker.unionlib.mod.UnionMod.LoadType.BOTH) {
			@Override
			@OnlyIn(Dist.CLIENT)
			public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
				return new ConfigScreen(previousScreen, CONFIG, new TranslatableComponent("UnionLib Config"));
			}

			@Override
			public List<Class<?>> getRegistries() {
				return Lists.newArrayList(UItems.class);
			}
		};
		for (int i = 0; i < 4; i++) {
			new UnionMod("concept_class"+i, location("textures/gui/test_1.png"), com.stereowalker.unionlib.mod.UnionMod.LoadType.CLIENT, !FMLEnvironment.production) {
				@Override
				@OnlyIn(Dist.CLIENT)
				public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
					return new ConfigScreen(previousScreen, TestClassConfig.class, new TranslatableComponent("Test Config 1"));
				}
			};
		}
		for (int i = 0; i < 4; i++) {
			new UnionMod("concept_object"+i, location("textures/gui/test_2.png"), com.stereowalker.unionlib.mod.UnionMod.LoadType.CLIENT, !FMLEnvironment.production) {
				@Override
				@OnlyIn(Dist.CLIENT)
				public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
					return new ConfigScreen(previousScreen, test_config, new TranslatableComponent("Test Config 2"));
				}
			};
		}

		boolean setupLoadLevel = false;
		for (UnionMod mod : mods) {
			///////////////////////////////////
			if (!setupLoadLevel) {
				if (mod.getLoadType() == LoadType.BOTH) {
					loadLevel = LoadType.BOTH;
					setupLoadLevel = true;
				} else if (mod.getLoadType() == LoadType.CLIENT) {
					if (loadLevel == LoadType.SERVER) {
						loadLevel = LoadType.BOTH;
						setupLoadLevel = true;
					} else {
						loadLevel = LoadType.CLIENT;
					}
				} else if (mod.getLoadType() == LoadType.SERVER) {
					if (loadLevel == LoadType.CLIENT) {
						loadLevel = LoadType.BOTH;
						setupLoadLevel = true;
					} else {
						loadLevel = LoadType.SERVER;
					}
				}
			}
			///////////////////////////////////
			for (Class<?> clas : mod.getRegistries()) {
				RegisterObjects.register(clas);
			}
		}
	}

	private void setup(final FMLCommonSetupEvent event)
	{
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		if (UnionLib.loadLevel != LoadType.CLIENT) {
			KeyBindings.registerKeyBindings();
			UScreens.registerScreens();
		}
	}

	public static ResourceLocation location(String name)
	{
		return new ResourceLocation(MOD_ID, name);
	}


	/**
	 * Returns the players accessory inventory. If this is ever edited, save it with {@link UnionLib#saveInventory(Player, UnionInventory)}
	 * @param player
	 * @return
	 */
	public static UnionInventory getAccessoryInventory (Player player) {
		UnionInventory inventory = new UnionInventory(player);
		inventory.read(player.getPersistentData().getList(INVENTORY_KEY, 10));
		return inventory;
	}

	/**
	 * Saves the accessory inventory to the players NBT. If this is not called after changes have been made, it will not save
	 * @param player
	 * @param inventory
	 */
	public static void saveInventory(Player player, UnionInventory inventory) {
		player.getPersistentData().put(INVENTORY_KEY, inventory.write());
	}
}
