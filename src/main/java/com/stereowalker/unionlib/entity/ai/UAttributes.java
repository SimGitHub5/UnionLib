package com.stereowalker.unionlib.entity.ai;

import com.stereowalker.unionlib.registries.RegistryHolder;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

@RegistryHolder("unionlib")
public class UAttributes {
	@RegistryHolder("generic.dig_speed")
	public static final Attribute DIG_SPEED = new RangedAttribute( "attribute.name.generic.dig_speed", 1.0D, -1024.0D, 1024.0D).setSyncable(true);
}
