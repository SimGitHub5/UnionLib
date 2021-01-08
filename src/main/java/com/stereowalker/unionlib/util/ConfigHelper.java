package com.stereowalker.unionlib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigHelper {

	public static ConfigValue<List<String>> listValue(String category, String name, ForgeConfigSpec.Builder builder, List<String> defaultValue, String... text){
		String comment = "";
		for(String re : text) {
			comment = comment+"\n"+re;
		}
		List<String> exampleValue = new ArrayList<String>();
		defaultValue.forEach((value) -> {
			exampleValue.add("\""+value+"\"");
		});
		return builder
				.comment(comment
						+ "\nDefault: "+exampleValue+"")
				.define(category+"."+name, defaultValue);
	}
	
	public static <T> ConfigValue<Map<String,T>> mapValue(String category, String name, ForgeConfigSpec.Builder builder, Map<String,T> defaultValue, String... text){
		String comment = "";
		for(String re : text) {
			comment = comment+"\n"+re;
		}
		List<String> exampleValue = new ArrayList<String>();
		defaultValue.forEach((key,value) -> {
			exampleValue.add("\""+key+","+value+"\"");
		});
		return builder
				.comment(comment
						+ "\nDefault: "+exampleValue+"")
				.define(category+"."+name, defaultValue);
	}
	
	public static DoubleValue chanceValue(String category, String name, ForgeConfigSpec.Builder builder, double defaultValue, String... text){
		String comment = "";
		for(String re : text) {
			comment = comment+"\n"+re;
		}
		return builder
				.comment(comment
						+ "\nDefault: "+defaultValue+"")
				.defineInRange(category+"."+name, defaultValue, 0.00000D, 1.00000D);
	}

	public static IntValue intValue(String category, String name, ForgeConfigSpec.Builder builder, int defaultValue, int min, int max, String... text){
		String comment = "";
		for(String re : text) {
			comment = comment+"\n"+re;
		}
		return builder
				.comment(comment
						+ "\nDefault: "+defaultValue+"")
				.defineInRange(category+"."+name, defaultValue, min, max);
	}
	
	public static DoubleValue doubleValue(String category, String name, ForgeConfigSpec.Builder builder, double defaultValue, double min, double max, String... text){
		String comment = "";
		for(String re : text) {
			comment = comment+"\n"+re;
		}
		return builder
				.comment(comment
						+ "\nDefault: "+defaultValue+"")
				.defineInRange(category+"."+name, defaultValue, min, max);
	}
	
	public static IntValue probablilityValue(String category, String name, ForgeConfigSpec.Builder builder, int defaultValue, String... text){
		return intValue(category, name, builder, defaultValue, 0, 1000, text);
	}
}
