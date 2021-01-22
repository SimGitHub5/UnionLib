package com.stereowalker.unionlib.config;

public interface CommentedEnum<T extends Enum<?>> {
	public abstract String getComment();

	default String getConfigComment() {
		String value = "";

		for(T enumValue : getValues()) {
			if (enumValue instanceof CommentedEnum) {
				value+= "\n"+enumValue.name()+": "+((CommentedEnum<?>)enumValue).getComment();
			}
		}

		return value;

	}

	public abstract T[] getValues();
}
