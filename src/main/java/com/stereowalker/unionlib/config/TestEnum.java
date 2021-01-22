package com.stereowalker.unionlib.config;

public enum TestEnum implements CommentedEnum<TestEnum> {
	CARD{
		@Override
		public String getComment() {
			return "Just a Card";
		}
	},
	
	WILD{
		@Override
		public String getComment() {
			return "Turns Into a Wild Animal";
		}
	};

	@Override
	public TestEnum[] getValues() {
		return TestEnum.values();
	}

}
