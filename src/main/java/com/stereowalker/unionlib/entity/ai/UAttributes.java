package com.stereowalker.unionlib.entity.ai;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class UAttributes {
	public static final IAttribute DIG_SPEED = (new RangedAttribute((IAttribute)null, "attribute.name.unionlib.dig_speed", 1.0D, -1024.0D, 1024.0D)).setShouldWatch(true);
}
