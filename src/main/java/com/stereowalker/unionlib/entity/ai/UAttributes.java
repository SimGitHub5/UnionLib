package com.stereowalker.unionlib.entity.ai;

import java.util.ArrayList;
import java.util.List;

import com.stereowalker.unionlib.UnionLib;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.IForgeRegistry;

public class UAttributes {
	public static final List<Attribute> ATTRIBUTE_REGISTRY = new ArrayList<Attribute>();
	
	public static final Attribute DIG_SPEED = register("generic.dig_speed", new RangedAttribute( "attribute.name.generic.dig_speed", 1.0D, -1024.0D, 1024.0D).setSyncable(true));;
			
	public static Attribute register(String name, Attribute attribute) {
		attribute.setRegistryName(UnionLib.location(name));
		ATTRIBUTE_REGISTRY.add(attribute);
		return attribute;
	}
	
	public static void registerAll(IForgeRegistry<Attribute> registry) {
		for(Attribute attribute : ATTRIBUTE_REGISTRY) {
			registry.register(attribute);
		}
	}
}
