package com.stereowalker.unionlib.config;

import com.stereowalker.unionlib.config.annotations.UnionConfig;
import com.stereowalker.unionlib.config.annotations.UnionConfigComment;
import com.stereowalker.unionlib.config.annotations.UnionConfigEntry;
import com.stereowalker.unionlib.config.annotations.UnionConfigRange;

@UnionConfig(name = "union")
public class Config {

	public static String ant1 = "HAPPY";

	@UnionConfigEntry(group = "MMAN" , name = "Happy Ant")
	public static String ant2 = "FAPPY";
	
	@UnionConfigEntry(group = "General" , name = "Debug")
	@UnionConfigComment(comment = "Enable this to see debug messages")
	public static boolean debug = false;
	
	@UnionConfigEntry(group = "General" , name = "River")
	@UnionConfigComment(comment = "Enable this to see ")
	public static int river = 5;
	
	@UnionConfigEntry(group = "General" , name = "Sea")
	@UnionConfigComment(comment = "Newton")
	@UnionConfigRange(min = 0, max = 100)
	public static int sea = 10;

}
