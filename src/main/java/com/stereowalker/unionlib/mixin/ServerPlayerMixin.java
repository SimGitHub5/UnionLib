package com.stereowalker.unionlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;
import com.stereowalker.unionlib.entity.player.CustomInventoryGetter;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

	public ServerPlayerMixin(Level p_36114_, BlockPos p_36115_, float p_36116_, GameProfile p_36117_) {
		super(p_36114_, p_36115_, p_36116_, p_36117_);
	}
	
	@Inject(method = "restoreFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;replaceWith(Lnet/minecraft/world/entity/player/Inventory;)V"))
	public void restoreFrom_inject (ServerPlayer pThat, boolean pKeepEverything, CallbackInfo ci) {
		((CustomInventoryGetter)this).getUnionInventory().replaceWith(((CustomInventoryGetter)pThat).getUnionInventory());
	}

}
