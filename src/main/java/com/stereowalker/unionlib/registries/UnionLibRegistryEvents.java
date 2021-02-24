package com.stereowalker.unionlib.registries;

import com.stereowalker.unionlib.inventory.container.UContainerType;
import com.stereowalker.unionlib.inventory.container.UnionContainer;
import com.stereowalker.unionlib.item.UItems;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class UnionLibRegistryEvents
{
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onTextureStitch(TextureStitchEvent.Pre event)
	{
		if(event.getMap().func_229223_g_().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE))
		{
			event.addSprite(UnionContainer.EMPTY_ACCESSORY_SLOT_RING);
			event.addSprite(UnionContainer.EMPTY_ACCESSORY_SLOT_NECKLACE);
		}
	}

	//Game Object Registries

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		UItems.registerAll(event.getRegistry());
	}
	
	@SubscribeEvent
	public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
		UContainerType.registerAll(event.getRegistry());
	}
}
