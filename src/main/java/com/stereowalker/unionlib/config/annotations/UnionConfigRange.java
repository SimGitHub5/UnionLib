/**
 * 
 */
package com.stereowalker.unionlib.config.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
/**
 * Meant to define the range of values when using numbered config entries
 * @author Stereowalker
 *
 */
public @interface UnionConfigRange {
	double min();
	double max();
}