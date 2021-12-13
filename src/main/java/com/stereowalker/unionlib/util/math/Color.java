package com.stereowalker.unionlib.util.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Color {
	float r, g, b, a;
	
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	/**
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public Color(int r, int g, int b, int a) {
		this(((float)r)/255.0f, ((float)g)/255.0f, ((float)b)/255.0f, ((float)a)/255.0f);
	}
	
	public Color(float r, float g, float b) {
		this(r, g, b, 1);
	}
	
	public Color(int r, int g, int b) {
		this(((float)r)/255.0f, ((float)g)/255.0f, ((float)b)/255.0f);
	}
}
