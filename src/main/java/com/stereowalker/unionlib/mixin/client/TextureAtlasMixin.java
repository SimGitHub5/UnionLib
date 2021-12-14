package com.stereowalker.unionlib.mixin.client;

import java.util.Set;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.stereowalker.unionlib.UnionLib;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.inventory.InventoryMenu;

@Mixin(TextureAtlas.class)
public abstract class TextureAtlasMixin extends AbstractTexture implements Tickable {
	@Shadow public ResourceLocation location() {return null;}
	
	@Inject(method = "prepareToStitch", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void prepareToStitch_inject (ResourceManager pResourceManager, Stream<ResourceLocation> pSpriteNames, ProfilerFiller pProfiler, int pMipLevel, CallbackInfoReturnable<TextureAtlas.Preparations> cir, Set<ResourceLocation> set) {
		if(this.location().equals(InventoryMenu.BLOCK_ATLAS))
		{
			set.add(UnionLib.Locations.EMPTY_ACCESSORY_SLOT_RING);
			set.add(UnionLib.Locations.EMPTY_ACCESSORY_SLOT_NECKLACE);
			UnionLib.debug("Stiching accessorires");
		}
	}
}
