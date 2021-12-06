package com.stereowalker.unionlib.config.tests;

import java.util.List;

import com.google.common.collect.Lists;
import com.stereowalker.unionlib.config.UnionConfig;

@UnionConfig(name = "union-test-bind-1")
public class TestBindClass1Config {

	@UnionConfig.Entry(name = "Test List 1")
	@UnionConfig.Comment(comment = {"Test One"})
	public static List<String> test_list = Lists.newArrayList("Arc", "Knight");
	
	@UnionConfig.Entry(name = "Test List 2")
	@UnionConfig.Comment(comment = {"Test Two"})
	public static List<Object> test_list_2 = Lists.newArrayList("Arc", 1);

}
