package com.stereowalker.unionlib.config;

import net.minecraftforge.fml.config.ModConfig.Type;

@UnionConfig(name = "union")
public class Config {

	@UnionConfig.Entry(group = "General" , name = "Debug")
	@UnionConfig.Comment(comment = {"Enable this to see debug messages","Added"})
	public static boolean debug = false;
	
	@UnionConfig.Entry(group = "General" , name = "Show Config Button", type = Type.CLIENT)
	@UnionConfig.Comment(comment = {"Enable this to show the unionlib config button in the main menu"})
	public static boolean config_button = true;
	
	@UnionConfig.Entry(group = "Capes" , name = "Show Capes", type = Type.CLIENT)
	@UnionConfig.Comment(comment = {"Enable this to show the cape bestowed upon you if you are an obsidian supporter"})
	public static boolean display_cape = true;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test String")
	public static String ant2 = "Default String Value Wtihout Coment";
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Integer")
	@UnionConfig.Comment(comment = "Test Comment")
	public static int river = 5;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Float")
	@UnionConfig.Comment(comment = {"Test Single-line comment"})
	@UnionConfig.Range(min = 0, max = 100)
	public static float sea = 10;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Long")
	@UnionConfig.Comment(comment = {"comment"})
	@UnionConfig.Range(min = 0, max = 1000)
	@UnionConfig.Slider
	public static long ha = 10;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Enum")
	@UnionConfig.Comment(comment = {"Test Multi-line comment","This is the second line"})
	public static TestEnum testEnum = TestEnum.CARD;

}
