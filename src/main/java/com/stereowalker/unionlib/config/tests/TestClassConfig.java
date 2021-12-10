package com.stereowalker.unionlib.config.tests;

import com.stereowalker.unionlib.config.TestEnum;
import com.stereowalker.unionlib.config.UnionConfig;

@UnionConfig(folder = "folder test", name = "union-test-class")
public class TestClassConfig {

	@UnionConfig.Entry(group = "Test Group 2" , name = "Debug")
	@UnionConfig.Comment(comment = {"Enable this to see debug messages","Added"})
	public static boolean test_boolean = false;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test String")
	public static String test_string = "Default String Value Wtihout Coment";
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Integer")
	@UnionConfig.Comment(comment = "Test Comment")
	public static int test_integer = 5;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Float")
	@UnionConfig.Comment(comment = {"Test Single-line comment"})
	@UnionConfig.Range(min = 0, max = 100)
	public static float test_float = 10;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Long")
	@UnionConfig.Comment(comment = {"comment"})
	@UnionConfig.Range(min = 0, max = 1000)
	@UnionConfig.Slider
	public static long test_long = 10;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Enum")
	@UnionConfig.Comment(comment = {"Test Multi-line comment","This is the second line"})
	public static TestEnum test_enum = TestEnum.CARD;

}
