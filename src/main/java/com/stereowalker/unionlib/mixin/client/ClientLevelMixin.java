package com.stereowalker.unionlib.mixin.client;

import java.io.File;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.config.ConfigBuilder;
import com.stereowalker.unionlib.supporter.CapeHandler;
import com.stereowalker.unionlib.supporter.Supporters;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraftforge.fml.config.ModConfig;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
	@Shadow @Final private Minecraft minecraft;

	protected ClientLevelMixin(WritableLevelData pLevelData, ResourceKey<Level> pDimension,
			DimensionType pDimensionType, Supplier<ProfilerFiller> pProfiler, boolean pIsClientSide, boolean pIsDebug,
			long pBiomeZoomSeed) {
		super(pLevelData, pDimension, pDimensionType, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"))
	public void init_inject(CallbackInfo ci) {
		ConfigBuilder.load(ModConfig.Type.COMMON, ModConfig.Type.CLIENT);
		UnionLib.debug("Loading All Client Config Files");
		System.out.println("Loading All Client Config Files");
	}
	
	@Inject(method = "addPlayer", at = @At("HEAD"))
	public void addPlayer_inject(int pPlayerId, AbstractClientPlayer pPlayerEntity, CallbackInfo ci) {
		Supporters.populateSupporters(new File(minecraft.gameDirectory, "supportercache.json"), false);
		CapeHandler.queuePlayerCapeReplacement(pPlayerEntity);
	}
	
}
