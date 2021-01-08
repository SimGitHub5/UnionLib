/**
 * 
 */
package com.stereowalker.unionlib.config.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
}