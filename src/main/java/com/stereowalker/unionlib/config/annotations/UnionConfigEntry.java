/**
 * 
 */
package com.stereowalker.unionlib.config.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.minecraftforge.fml.config.ModConfig;

@Retention(RUNTIME)
@Target(FIELD)
/**
 * @author Stereowalker
 *
 */
public @interface UnionConfigEntry {
	String name();
	String group();
	ModConfig.Type type()default ModConfig.Type.COMMON;}
