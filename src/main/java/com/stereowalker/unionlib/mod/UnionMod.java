package com.stereowalker.unionlib.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.stereowalker.unionlib.UnionLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.MavenVersionStringHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class UnionMod {
	private String modid;
	private LoadType loadType;
	private ResourceLocation modTexture;
	private ModInfo modInfo;
	private final String NETWORK_PROTOCOL_VERSION = "1";
	public final Logger LOGGER = LogManager.getLogger(modid);
	public final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(location("main"), () -> NETWORK_PROTOCOL_VERSION, NETWORK_PROTOCOL_VERSION::equals, NETWORK_PROTOCOL_VERSION::equals);
	private int netID = -1;

	public UnionMod(String modid, ResourceLocation modTexture, LoadType loadType) {
		this.modid = modid;
		this.modTexture = modTexture;
		this.loadType = loadType;
		UnionLib.mods.add(this);
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> {
			return (minecraft, parentScreen) -> {
				return getConfigScreen(minecraft, parentScreen);
			};
		});
		registerMessages(CHANNEL);
	}

	public LoadType getLoadType() {
		return loadType;
	}

	public String getModid() {
		return modid;
	}

	public ResourceLocation getModTexture() {
		return modTexture;
	}

	@OnlyIn(Dist.CLIENT)
	public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
		return null;
	}

	public ModInfo getModInfo() {
		ModList.get().getMods().forEach((mod) -> {
			if (mod.getModId() == getModid()) {
				modInfo = mod;
			}
		});
		return modInfo;
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
