package com.stereowalker.unionlib.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.stereowalker.unionlib.UnionLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import net.minecraftforge.fmllegacy.MavenVersionStringHelper;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import net.minecraftforge.forgespi.language.IModInfo;

public class UnionMod {
	private String modid;
	private LoadType loadType;
	private ResourceLocation modTexture;
	private IModInfo modInfo;
	private final String NETWORK_PROTOCOL_VERSION = "1";

	public final SimpleChannel channel;
	private int netID = -1;

	public UnionMod(String modid, ResourceLocation modTexture, LoadType loadType, boolean shouldLoadMod) {
		this.modid = modid;
		this.modTexture = modTexture;
		this.loadType = loadType;
		this.channel = NetworkRegistry.newSimpleChannel(location("main_simple_channel"), () -> NETWORK_PROTOCOL_VERSION, NETWORK_PROTOCOL_VERSION::equals, NETWORK_PROTOCOL_VERSION::equals);
		if (shouldLoadMod) {
			UnionLib.mods.add(this);
			ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> {
				return new ConfigGuiHandler.ConfigGuiFactory((minecraft, parentScreen) -> {
					return getConfigScreen(minecraft, parentScreen);
				});

			});
			registerMessages(channel);
		}
	}

	public UnionMod(String modid, ResourceLocation modTexture, LoadType loadType) {
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
