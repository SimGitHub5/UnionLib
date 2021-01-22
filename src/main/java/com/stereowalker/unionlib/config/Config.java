package com.stereowalker.unionlib.config;

@UnionConfig(name = "union")
public class Config {

	@UnionConfig.Entry(group = "General" , name = "Debug")
	@UnionConfig.Comment(comment = {"Enable this to see debug messages","Added"})
	public static boolean debug = false;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test String")
	public static String ant2 = "Default String Value Wtihout Coment";
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Integer")
	@UnionConfig.Comment(comment = "Test Comment")
	public static int river = 5;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Float")
	@UnionConfig.Comment(comment = {"Test Single-line comment"})
	@UnionConfig.Range(min = 0, max = 100)
	public static float sea = 10;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Enum")
	@UnionConfig.Comment(comment = {"Test Multi-line comment","This is the second line"})
	public static TestEnum testEnum = TestEnum.CARD;

}
