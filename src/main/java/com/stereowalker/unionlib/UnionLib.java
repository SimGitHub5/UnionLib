package com.stereowalker.unionlib;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import com.stereowalker.unionlib.client.gui.screen.inventory.UScreens;
import com.stereowalker.unionlib.client.gui.screens.config.ConfigScreen;
import com.stereowalker.unionlib.client.gui.screens.config.MinecraftModConfigsScreen;
import com.stereowalker.unionlib.client.keybindings.KeyBindings;
import com.stereowalker.unionlib.config.Config;
import com.stereowalker.unionlib.config.ConfigBuilder;
import com.stereowalker.unionlib.config.ServerConfig;
import com.stereowalker.unionlib.config.tests.TestBindClass1Config;
import com.stereowalker.unionlib.config.tests.TestBindClass2Config;
import com.stereowalker.unionlib.config.tests.TestClassConfig;
import com.stereowalker.unionlib.config.tests.TestObjectConfig;
import com.stereowalker.unionlib.entity.ai.UAttributes;
import com.stereowalker.unionlib.inventory.container.UContainerType;
import com.stereowalker.unionlib.item.UItems;
import com.stereowalker.unionlib.mod.MinecraftMod;
import com.stereowalker.unionlib.mod.ModHandler;
import com.stereowalker.unionlib.network.PacketRegistry;
import com.stereowalker.unionlib.supporter.Supporters;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class UnionLib extends MinecraftMod {

	public static UnionLib instance;
	public static TestObjectConfig test_config = new TestObjectConfig();
	public static final Config CONFIG = new Config();
	public static final ServerConfig SERVER_CONFIG = new ServerConfig();
	public static final String MOD_ID = "unionlib";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final String INVENTORY_KEY = "UnionInventory";
	//	private static final String NETWORK_PROTOCOL_VERSION = "1";
	public static LoadType loadLevel = null;
	//	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(location("main"), () -> NETWORK_PROTOCOL_VERSION, NETWORK_PROTOCOL_VERSION::equals, NETWORK_PROTOCOL_VERSION::equals);

	public static void debug(Object message) {
		if (CONFIG.debug) {
			UnionLib.LOGGER.debug(message);
		}
	}

	public static void warn(String message) {
		if (CONFIG.debug) {
			UnionLib.LOGGER.warn(message);
		}
	}

	public static boolean disableConfig() {
		return false;
	}

	public UnionLib() 
	{
		super("unionlib", Locations.UNION_BUTTON_IMAGE, LoadType.BOTH);
		instance = this;
	}

	@Override
	public void onModStartup() {
		ConfigBuilder.registerConfig(getModid(), CONFIG);
		ConfigBuilder.registerConfig(getModid(), SERVER_CONFIG);
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			ConfigBuilder.registerConfig(getModid(), TestClassConfig.class);
			ConfigBuilder.registerConfig(getModid(), TestBindClass1Config.class);
			ConfigBuilder.registerConfig(getModid(), TestBindClass2Config.class);
			ConfigBuilder.registerConfig(getModid(), test_config);
		}
		PacketRegistry.registerServerboundListeners();

		new MinecraftMod("concept_class", location("textures/gui/test_1.png"), MinecraftMod.LoadType.CLIENT, FabricLoader.getInstance().isDevelopmentEnvironment()) {
			@Override
			@Environment(EnvType.CLIENT)
			public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
				return new ConfigScreen(previousScreen, TestClassConfig.class, new TranslatableComponent("Test Config 1"));
			}
		};
		new MinecraftMod("concept_object", location("textures/gui/test_2.png"), MinecraftMod.LoadType.CLIENT, FabricLoader.getInstance().isDevelopmentEnvironment()) {
			@Override
			@Environment(EnvType.CLIENT)
			public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
				return new ConfigScreen(previousScreen, test_config, new TranslatableComponent("Test Config 2"));
			}
		};
		new MinecraftMod("concept_combined_config", location("textures/gui/test_3.png"), MinecraftMod.LoadType.CLIENT, FabricLoader.getInstance().isDevelopmentEnvironment()) {
			@Override
			@Environment(EnvType.CLIENT)
			public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
				return new MinecraftModConfigsScreen(previousScreen, new TranslatableComponent("Test Config 3"), TestBindClass1Config.class, TestBindClass2Config.class);
			}
		};
		new MinecraftMod("concept_keys", location("textures/gui/test_4.png"), MinecraftMod.LoadType.CLIENT, FabricLoader.getInstance().isDevelopmentEnvironment()) {
			@Override
			public KeyMapping[] getModKeyMappings() {
				return new KeyMapping[]{new KeyMapping("key.unionlib.test_bind", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, "DAD")};
			}
		};
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
		return new ConfigScreen(previousScreen, UnionLib.CONFIG, new TranslatableComponent("UnionLib Config"));
	}

	@Override
	public List<Class<?>> getRegistries() {
		if (UnionLib.loadLevel != LoadType.CLIENT)
			return Lists.newArrayList(UAttributes.class, UItems.class, UContainerType.class);
		else 
			return Lists.newArrayList();
	}

	@Override
	public KeyMapping[] getModKeyMappings() {
		return new KeyMapping[]{KeyBindings.OPEN_UNION_INVENTORY};
	}

	@Override
	public Map<EntityType<? extends LivingEntity>, List<Attribute>> appendAttributesWithoutValues() {
		Map<EntityType<? extends LivingEntity>, List<Attribute>> map = Maps.newHashMap();
		map.put(EntityType.PLAYER, Lists.newArrayList(UAttributes.DIG_SPEED));
		return map;
	}

	@SuppressWarnings("resource")
	@Override
	@Environment(EnvType.CLIENT)
	public void onModStartupInClient() {
		Supporters.populateSupporters(new File(Minecraft.getInstance().gameDirectory, "supportercache.json"), true);
		ModHandler.mods.values().forEach((mod) -> Lists.newArrayList(mod.getModKeyMappings()).forEach(KeyBindingHelper::registerKeyBinding));
		if (UnionLib.loadLevel != LoadType.CLIENT) {
			UScreens.registerScreens();
		}
	}

	public static class Locations {
		public static final ResourceLocation UNION_BUTTON_IMAGE = new ResourceLocation(UnionLib.MOD_ID, "textures/gui/union_button.png");
		public static final ResourceLocation EMPTY_ACCESSORY_SLOT_NECKLACE = new ResourceLocation(UnionLib.MOD_ID, "item/empty_accessory_slot_necklace");
		public static final ResourceLocation EMPTY_ACCESSORY_SLOT_RING = new ResourceLocation(UnionLib.MOD_ID, "item/empty_accessory_slot_ring");
		public static final ResourceLocation UNION_INVENTORY_BACKGROUND = new ResourceLocation(UnionLib.MOD_ID, "textures/gui/container/union_inventory.png");
	}
}
