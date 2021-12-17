package com.stereowalker.unionlib.registries;

import java.lang.reflect.Field;
import java.util.function.Consumer;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.mod.MinecraftMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RegisterObjects {

	public static void putObjectsInForgeRegistries(MinecraftMod mod) {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		for (Class<?> classs : mod.getRegistries()) {
			UnionLib.debug("UnionLib Registry: Found class "+classs);
			boolean hasAnnotation = classs.isAnnotationPresent(RegistryHolder.class);
			final RegistryHolder reg = hasAnnotation ? classs.getAnnotation(RegistryHolder.class) : null;
			for (Field field : classs.getFields()) {
				if (field.isAnnotationPresent(RegistryHolder.class)) {
					RegistryHolder regi = field.getAnnotation(RegistryHolder.class);
					try {
						if (field.get(null) != null && field.get(null) instanceof Fluid) { modEventBus.addGenericListener(Fluid.class, (Consumer<RegistryEvent.Register<Fluid>>)event-> {
							try {
								Fluid fluid;
								fluid = (Fluid)field.get(null);
								event.getRegistry().register(fluid.setRegistryName(new ResourceLocation(hasAnnotation ? reg.value()+":"+regi.value() : regi.value())));
							} catch (IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
						});
						}
						else if (field.get(null) != null && field.get(null) instanceof Item) { modEventBus.addGenericListener(Item.class, (Consumer<RegistryEvent.Register<Item>>)event-> {
							try {
								Item item;
								item = (Item)field.get(null);
								event.getRegistry().register(item.setRegistryName(new ResourceLocation(hasAnnotation ? reg.value()+":"+regi.value() : regi.value())));
							} catch (IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
						});
						}
						else if (field.get(null) != null && field.get(null) instanceof Attribute) { modEventBus.addGenericListener(Attribute.class, (Consumer<RegistryEvent.Register<Attribute>>)event-> {
							try {
								Attribute attribute;
								attribute = (Attribute)field.get(null);
								event.getRegistry().register(attribute.setRegistryName(new ResourceLocation(hasAnnotation ? reg.value()+":"+regi.value() : regi.value())));
							} catch (IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
						});
						}
						else if (field.get(null) != null && field.get(null) instanceof MenuType<?>) { modEventBus.addGenericListener(MenuType.class, (Consumer<RegistryEvent.Register<MenuType<?>>>)event-> {
							try {
								MenuType<?> menuType;
								menuType = (MenuType<?>)field.get(null);
								event.getRegistry().register(menuType.setRegistryName(new ResourceLocation(hasAnnotation ? reg.value()+":"+regi.value() : regi.value())));
							} catch (IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
						});
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
