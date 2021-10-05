package com.stereowalker.unionlib.registries;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.entity.ai.UAttributes;
import com.stereowalker.unionlib.inventory.container.UContainerType;
import com.stereowalker.unionlib.inventory.container.UnionContainer;
import com.stereowalker.unionlib.mod.UnionMod.LoadType;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
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
		if(event.getMap().location().equals(TextureAtlas.LOCATION_BLOCKS))
		{
			event.addSprite(UnionContainer.EMPTY_ACCESSORY_SLOT_RING);
			event.addSprite(UnionContainer.EMPTY_ACCESSORY_SLOT_NECKLACE);
		}
	}

	//Game Object Registries

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		if (UnionLib.loadLevel != LoadType.CLIENT)
			RegisterObjects.putItemsInForgeRegistries(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerContainers(final RegistryEvent.Register<MenuType<?>> event) {
		if (UnionLib.loadLevel != LoadType.CLIENT)
			UContainerType.registerAll(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeModificationEvent event) {
		if (UnionLib.loadLevel != LoadType.CLIENT)
			event.add(EntityType.PLAYER, UAttributes.DIG_SPEED);
	}
}
