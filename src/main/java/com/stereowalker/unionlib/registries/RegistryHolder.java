/**
 * 
 */
package com.stereowalker.unionlib.registries;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
/**
 * @author Stereowalker
 *
 */
public @interface RegistryHolder {
	/**
	 * @return The name of the item
	 */
	String value();
}