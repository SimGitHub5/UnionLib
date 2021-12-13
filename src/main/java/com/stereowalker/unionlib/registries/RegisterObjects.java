package com.stereowalker.unionlib.registries;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.stereowalker.unionlib.UnionLib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RegisterObjects {
	static final List<Class<?>> registries = Lists.newArrayList();

	public static void register(Class<?> re) {
		registries.add(re);
	}

	public static void putObjectsInForgeRegistries() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		for (Class<?> classs : registries) {
			UnionLib.debug("UnionLib Registry: Found class "+classs);
			boolean hasAnnotation = classs.isAnnotationPresent(RegistryHolder.class);
			final RegistryHolder reg = hasAnnotation ? classs.getAnnotation(RegistryHolder.class) : null;
			for (Field field : classs.getFields()) {
				if (field.isAnnotationPresent(RegistryHolder.class)) {
					RegistryHolder regi = field.getAnnotation(RegistryHolder.class);
					try {
						if (field.get(null) != null && field.get(null) instanceof Item) { modEventBus.addGenericListener(Item.class, (Consumer<RegistryEvent.Register<Item>>)event-> {
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
