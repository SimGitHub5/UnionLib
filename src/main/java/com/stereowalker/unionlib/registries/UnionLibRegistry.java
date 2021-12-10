package com.stereowalker.unionlib.registries;

import java.util.function.Consumer;

import com.google.common.collect.Maps;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.entity.ai.UAttributes;
import com.stereowalker.unionlib.inventory.container.UContainerType;
import com.stereowalker.unionlib.mod.MinecraftMod.LoadType;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class UnionLibRegistry
{
	public static void registerObjects() {
		if (UnionLib.loadLevel != LoadType.CLIENT) {
			final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
			modEventBus.addGenericListener(Attribute.class, (Consumer<RegistryEvent.Register<Attribute>>)event-> {
				UAttributes.registerAll(event.getRegistry());
			});
			modEventBus.addGenericListener(MenuType.class, (Consumer<RegistryEvent.Register<MenuType<?>>>)event-> {
				UContainerType.registerAll(event.getRegistry());
			});
			modEventBus.addGenericListener(Item.class, (Consumer<RegistryEvent.Register<Item>>)event-> {
				RegisterObjects.putItemsInForgeRegistries(event.getRegistry());
			});
			modEventBus.addListener((Consumer<EntityAttributeModificationEvent>)event-> {
				UnionLib.mods.forEach((mod) -> Maps.newHashMap(mod.appendAttributesWithoutValues()).forEach((ent, ma) -> ma.forEach((attr) -> event.add(ent, attr))));
				UnionLib.mods.forEach((mod) -> Maps.newHashMap(mod.appendAttributesWithValues()).forEach((ent, ma) -> ma.forEach((attr) -> event.add(ent, attr.getA(), attr.getB()))));
			});
		}
	}
}
