package com.stereowalker.unionlib.mod;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stereowalker.unionlib.UnionLib;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class MinecraftMod {
	private String modid;
	private LoadType loadType;
	private ResourceLocation modTexture;
	private IModInfo modInfo;
	private final String NETWORK_PROTOCOL_VERSION = "1";

	public final SimpleChannel channel;
	protected int netID = -1;

	public MinecraftMod(String modid, ResourceLocation modTexture, LoadType loadType, boolean shouldLoadMod) {
		this.modid = modid;
		this.modTexture = modTexture;
		this.loadType = loadType;
		if (shouldLoadMod && !ModHandler.isModRegistered(modid)) {
			System.out.println("REGISTERED HERE");
			onModStartup();
			ModHandler.registerMod(this);
			ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> {
				return new ConfigGuiHandler.ConfigGuiFactory((minecraft, parentScreen) -> {
					return getConfigScreen(minecraft, parentScreen);
				});

			});
			if (this.loadType != LoadType.CLIENT) {
				this.channel = NetworkRegistry.newSimpleChannel(location("main_simple_channel"), () -> NETWORK_PROTOCOL_VERSION, NETWORK_PROTOCOL_VERSION::equals, NETWORK_PROTOCOL_VERSION::equals);
				registerMessages(channel);
			}
			else
				this.channel = null;
		}
		else
			this.channel = null;
	}
	
	public void onModStartup() {
	}

	public void onModStartupInClient() {
	}

	public MinecraftMod(String modid, ResourceLocation modTexture, LoadType loadType) {
		this(modid, modTexture, loadType, true);
	}
	
	public LoadType getLoadType() {
		return loadType;
	}

	public String getModid() {
		return modid;
	}

	public Logger getLogger() {
		return LogManager.getLogger(modid);
	}

	public ResourceLocation getModTexture() {
		return modTexture;
	}

	@OnlyIn(Dist.CLIENT)
	public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
		return null;
	}
	
	@OnlyIn(Dist.CLIENT)
	public KeyMapping[] getModKeyMappings() {
		return new KeyMapping[0];
	}

	public ResourceLocation location(String name)
	{
		return new ResourceLocation(modid, name);
	}

	public String locationString(String name)
	{
		return modid+":"+name;
	}

	public void registerMessages(SimpleChannel channel) {

	}
	
	public Map<EntityType<? extends LivingEntity>, List<Attribute>> appendAttributesWithoutValues() {
		return Maps.newHashMap();
	}
	
	public Map<EntityType<? extends LivingEntity>, List<Tuple<Attribute, Double>>> appendAttributesWithValues() {
		return Maps.newHashMap();
	}
	
	public List<Class<?>> getRegistries() {
		return Lists.newArrayList();
	}

	public IModInfo getModInfo() {
		ModList.get().getMods().forEach((mod) -> {
			if (mod.getModId() == getModid()) {
				modInfo = mod;
			}
		});
		return modInfo;
	}

	public String getVersion() {
		if (getModInfo() != null) {
			return MavenVersionStringHelper.artifactVersionToString(getModInfo().getVersion());
		}
		else return "???";
	}

	public static enum LoadType{
		CLIENT,BOTH,SERVER;
	}
}
