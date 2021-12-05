package com.stereowalker.unionlib.config;

import net.minecraftforge.fml.config.ModConfig.Type;

@UnionConfig(name = "unionlib")
public class Config implements ConfigObject {

	@UnionConfig.Entry(group = "General" , name = "Debug")
	@UnionConfig.Comment(comment = {"Enable this to see debug messages"})
	public boolean debug = false;
	
	@UnionConfig.Entry(group = "General" , name = "OpenGL Debug")
	@UnionConfig.Comment(comment = {"Enable this stop getting OpenGL messages","Useful for when you're constantly getting spammed with them"})
	public boolean openGL_debug = false;
	
	@UnionConfig.Entry(group = "General" , name = "Show Config Button", type = Type.CLIENT)
	@UnionConfig.Comment(comment = {"Enable this to show the unionlib config button in the main menu"})
	public boolean config_button = true;
	
	@UnionConfig.Entry(group = "Capes" , name = "Show Capes", type = Type.CLIENT)
	@UnionConfig.Comment(comment = {"Enable this to show the cape bestowed upon you if you are an obsidian supporter"})
	public boolean display_cape = true;
	
	@UnionConfig.Entry(group = "Menus" , name = "Text Overflow Scroll Speed", type = Type.CLIENT)
	@UnionConfig.Comment(comment = {"This determines how fast text scrolls if there is an overflow","Measured in ticks"})
	@UnionConfig.Slider
	@UnionConfig.Range(min = 1, max = 100)
	public int textScrollSpeed = 20;
	
	@UnionConfig.Entry(group = "Menus" , name = "Text Overflow Reset Time", type = Type.CLIENT)
	@UnionConfig.Comment(comment = {"This determines how long text will stay in an overflow before it resets","Measured in ticks"})
	@UnionConfig.Slider
	@UnionConfig.Range(min = 1, max = 100)
	public int textScrollReset = 40;

}
