package com.stereowalker.unionlib.mixin.client;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.WindowEventHandler;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.client.keybindings.KeyBindings;
import com.stereowalker.unionlib.mod.MinecraftMod.LoadType;
import com.stereowalker.unionlib.network.client.play.CUnionInventoryPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin extends ReentrantBlockableEventLoop<Runnable> implements WindowEventHandler {
	
	@Shadow @Nullable public LocalPlayer player;
	@Shadow @Nullable public Screen screen;

	public MinecraftMixin(String p_18765_) {
		super(p_18765_);
	}
	
	@Inject(method = "handleKeybinds", at = @At("TAIL"))
	public void tick_inject(CallbackInfo ci) {
		if (UnionLib.loadLevel != LoadType.CLIENT) {
			if(this.screen==null) {
				LocalPlayer clientPlayer = this.player;
				
				if (KeyBindings.OPEN_UNION_INVENTORY.consumeClick()) {
					new CUnionInventoryPacket(clientPlayer.getUUID()).send();
				}
			}
		}
	}

}
