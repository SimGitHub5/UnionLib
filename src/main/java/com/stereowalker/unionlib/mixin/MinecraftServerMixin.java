package com.stereowalker.unionlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.config.ConfigBuilder;

import net.minecraft.commands.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraftforge.fml.config.ModConfig;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantBlockableEventLoop<TickTask> implements CommandSource, AutoCloseable {

	public MinecraftServerMixin(String p_18765_) {
		super(p_18765_);
	}
	
	@Inject(method = "createLevels", at = @At(value = "INVOKE_ASSIGN", ordinal = 0, target = "Lnet/minecraft/server/level/ServerLevel;getWorldBorder()Lnet/minecraft/world/level/border/WorldBorder;"))
	public void createLevels_inject(ChunkProgressListener p_129816_, CallbackInfo ci){
		ConfigBuilder.load(ModConfig.Type.COMMON, ModConfig.Type.SERVER);
		UnionLib.debug("Loading All Server Config Files");
		System.out.println("Loading All Server Config Files");
	}
}
