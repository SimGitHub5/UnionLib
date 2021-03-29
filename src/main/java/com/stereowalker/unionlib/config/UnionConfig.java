/**
 * 
 */
package com.stereowalker.unionlib.config;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.minecraftforge.fml.config.ModConfig;

@Retention(RUNTIME)
@Target(TYPE)
/**
 * @author Stereowalker
 *
 */
public @interface UnionConfig {
	/**
	 * @return The name of the config file itself
	 */
	String name();
	/**
	 * The name of the folder the files of this config will be within the config folder. 
	 * If this is empty, it will just put the files in the config folder
	 * @return The folder
	 */
	String folder()default "";
	/**
	 * This determines if the config file should have the type of the config appended to it.
	 * Client configs will have ".client.toml" attached to the name,
	 * Common configs will have ".common.toml" attached to the name,
	 * Server configs will have ".server.toml" attached to the name.
	 * If it is set to false, just ".toml" will be attached
	 */
	boolean appendWithType() default true;
	/**
	 * Should the config entries reload their values when the config file is edited when the game is running? 
	 */
	boolean autoReload() default true;
	
	@Retention(RUNTIME)
	@Target(FIELD)
	/**
	 * @author Stereowalker
	 *
	 */
	public @interface Entry {
		String name();
		String group();
		ModConfig.Type type()default ModConfig.Type.COMMON;
	}
	
	@Retention(RUNTIME)
	@Target(FIELD)
	/**
	 * Meant to define the range of values when using numbered config entries
	 * @author Stereowalker
	 *
	 */
	public @interface Range {
		double min();
		double max();
	}
	
	@Retention(RUNTIME)
	@Target(FIELD)
	/**
	 * Use this annotation if you want your sonfig to use a slider instead in the config gui
	 * @author Stereowalker
	 *
	 */
	public @interface Slider {
	}
	
	@Retention(RUNTIME)
	@Target(FIELD)
	/**
	 * @author Stereowalker
	 *
	 */
	public @interface Comment {
		String[] comment();
	}
}