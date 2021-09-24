package com.stereowalker.unionlib.config;

@UnionConfig(name = "union-test-object")
public class TestObjectConfig implements ConfigObject {
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test String")
	public String test_string = "Default String Value Wtihout Coment";
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Integer")
	@UnionConfig.Comment(comment = "Test Comment")
	public int test_integer = 5;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Float")
	@UnionConfig.Comment(comment = {"Test Single-line comment"})
	@UnionConfig.Range(min = 0, max = 100)
	public float test_float = 10;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Long")
	@UnionConfig.Comment(comment = {"comment"})
	@UnionConfig.Range(min = 0, max = 1000)
	@UnionConfig.Slider
	public long test_long = 10;
	
	@UnionConfig.Entry(group = "Test Group" , name = "Test Enum")
	@UnionConfig.Comment(comment = {"Test Multi-line comment","This is the second line"})
	public TestEnum test_enum = TestEnum.CARD;

}
