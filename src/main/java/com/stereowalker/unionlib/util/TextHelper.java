package com.stereowalker.unionlib.util;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TextHelper {
	public static boolean isFirstLeterVowel(String text) {
		return text.toLowerCase().charAt(0) == 'a' || text.toLowerCase().charAt(0) == 'e' || text.toLowerCase().charAt(0) == 'i' || text.toLowerCase().charAt(0) == 'o' || text.toLowerCase().charAt(0) == 'u';
	}

	public static boolean isFirstLeterVowel(ITextComponent component) {
		return isFirstLeterVowel(component.getString());
	}

	public static String articulatedText(String text, boolean iaArticleCapital) {
		if (iaArticleCapital) return isFirstLeterVowel(text) ? "An "+text : "A "+text;
		else return isFirstLeterVowel(text) ? "an "+text : "a "+text;
	}

	public static ITextComponent articulatedComponent(ITextComponent component, boolean iaArticleCapital, boolean shouldArticleUseComponentStyle) {
		IFormattableTextComponent comp;
		
		if (iaArticleCapital) {
			comp = new StringTextComponent(isFirstLeterVowel(component) ? "An " : "A ");
		}
		else {
			comp = new StringTextComponent(isFirstLeterVowel(component) ? "an " : "a ");
		}
		
		if (shouldArticleUseComponentStyle && component.getStyle() != null) {
			comp = (comp.mergeStyle(component.getStyle())).appendSibling(component);
		}
		else {
			comp = comp.appendSibling(component);
		}
		
		return comp;
	}
}
