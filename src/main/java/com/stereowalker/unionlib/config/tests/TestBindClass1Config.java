package com.stereowalker.unionlib.config.tests;

import com.stereowalker.unionlib.config.UnionConfig;

@UnionConfig(name = "union-test-bind-1")
public class TestBindClass1Config {

	@UnionConfig.Entry(name = "DDES")
	@UnionConfig.Comment(comment = {"Enable this to see debug messages","Added"})
	public static boolean test_2_boolean = false;

}
