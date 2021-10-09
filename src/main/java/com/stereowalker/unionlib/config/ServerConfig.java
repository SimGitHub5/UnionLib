package com.stereowalker.unionlib.config;

import net.minecraftforge.fml.config.ModConfig.Type;

@UnionConfig(name = "unionlib")
public class ServerConfig implements ConfigObject {

	@UnionConfig.Entry(group = "General" , name = "Show Config Button", type = Type.SERVER)
	@UnionConfig.Comment(comment = {"Enable this to show the unionlib config button in the main menu"})
	public boolean config_button = true;
	
	@UnionConfig.Entry(group = "Capes" , name = "Show Capes", type = Type.SERVER)
	@UnionConfig.Comment(comment = {"Enable this to show the cape bestowed upon you if you are an obsidian supporter"})
	public boolean display_cape = true;

}
