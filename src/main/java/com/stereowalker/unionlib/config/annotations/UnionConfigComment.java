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
 * @author Stereowalker
 *
 */
public @interface UnionConfigComment {
	String comment();
}
