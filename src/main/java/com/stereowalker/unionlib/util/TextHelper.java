package com.stereowalker.unionlib.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

public class TextHelper {
	public static boolean isFirstLeterVowel(String text) {
		return text.toLowerCase().charAt(0) == 'a' || text.toLowerCase().charAt(0) == 'e' || text.toLowerCase().charAt(0) == 'i' || text.toLowerCase().charAt(0) == 'o' || text.toLowerCase().charAt(0) == 'u';
	}

	public static boolean isFirstLeterVowel(Component component) {
		return isFirstLeterVowel(component.getString());
	}

	public static String articulatedText(String text, boolean iaArticleCapital) {
		if (iaArticleCapital) return isFirstLeterVowel(text) ? "An "+text : "A "+text;
		else return isFirstLeterVowel(text) ? "an "+text : "a "+text;
	}

	public static Component articulatedComponent(Component component, boolean iaArticleCapital, boolean shouldArticleUseComponentStyle) {
		MutableComponent comp;
		
		if (iaArticleCapital) {
			comp = new TextComponent(isFirstLeterVowel(component) ? "An " : "A ");
		}
		else {
			comp = new TextComponent(isFirstLeterVowel(component) ? "an " : "a ");
		}
		
		if (shouldArticleUseComponentStyle && component.getStyle() != null) {
			comp = (comp.withStyle(component.getStyle())).append(component);
		}
		else {
			comp = comp.append(component);
		}
		
		return comp;
	}
}
