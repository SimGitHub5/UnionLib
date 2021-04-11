package com.stereowalker.unionlib.entity.ai;

import com.stereowalker.unionlib.UnionLib;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class UAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTE_REGISTRY = DeferredRegister.create(Attribute.class, UnionLib.MOD_ID);
	
	public static final Attribute DIG_SPEED = register("generic.dig_speed", new RangedAttribute( "attribute.name.generic.dig_speed", 1.0D, -1024.0D, 1024.0D).setShouldWatch(true));;
			
	public static Attribute register(String name, Attribute attribute) {
		ATTRIBUTE_REGISTRY.register(name, () -> attribute);
		return attribute;
	}
	
	public static void registerAll(IEventBus eventBus) {
		ATTRIBUTE_REGISTRY.register(eventBus);
	}
}
