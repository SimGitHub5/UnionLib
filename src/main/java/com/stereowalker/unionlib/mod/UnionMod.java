package com.stereowalker.unionlib.mod;

import com.stereowalker.unionlib.UnionLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.MavenVersionStringHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

public class UnionMod {
	String modid;
	LoadType loadType;
	ResourceLocation modTexture;
	ModInfo modInfo;

	public UnionMod(String modid, ResourceLocation modTexture, LoadType loadType) {
		this.modid = modid;
		this.modTexture = modTexture;
		this.loadType = loadType;
		UnionLib.mods.add(this);
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
