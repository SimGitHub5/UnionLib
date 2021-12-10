package com.stereowalker.unionlib.mixin.client;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.unionlib.ClientCape;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.config.ConfigBuilder;
import com.stereowalker.unionlib.network.client.play.CQueueCapePacket;
import com.stereowalker.unionlib.network.client.play.CUpdateCapeListPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraftforge.fml.config.ModConfig;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {

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
		if(ClientCape.doesPlayerNeedCapeClient(pPlayerEntity) && pPlayerEntity == Minecraft.getInstance().player) {
			//Update the server on who would prefer to display their cape or not
			new CUpdateCapeListPacket(Player.createPlayerUUID((pPlayerEntity).getGameProfile()), UnionLib.CONFIG.display_cape).send();
		}
		new CQueueCapePacket().send();
	}
	
}
