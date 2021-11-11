package com.stereowalker.unionlib.config.tests;

import com.stereowalker.unionlib.config.UnionConfig;

@UnionConfig(name = "union-test-bind-2")
public class TestBindClass2Config {

	@UnionConfig.Entry(group = "Test Combinde Double" , name = "Double Test")
	@UnionConfig.Comment(comment = {"Enable this to see debug messages","Added"})
	public static double test_2_boolean = 50.0D;

}
