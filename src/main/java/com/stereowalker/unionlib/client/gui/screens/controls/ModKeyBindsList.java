package com.stereowalker.unionlib.client.gui.screens.controls;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class ModKeyBindsList extends KeyBindsList {

	public ModKeyBindsList(ModControlsScreen p_97399_, Minecraft p_97400_) {
		super(p_97399_, p_97400_);
		this.children().removeAll(this.children());

		KeyMapping[] akeymapping = ArrayUtils.clone(p_97399_.mod.getModKeyMappings());
		Arrays.sort((Object[])akeymapping);
		String s = null;

		for(KeyMapping keymapping : akeymapping) {
			String s1 = keymapping.getCategory();
			if (!s1.equals(s)) {
				s = s1;
				this.addEntry(new KeyBindsList.CategoryEntry(new TranslatableComponent(s1)));
			}

			Component component = new TranslatableComponent(keymapping.getName());
			int i = p_97400_.font.width(component);
			if (i > this.maxNameWidth) {
				this.maxNameWidth = i;
			}

			this.addEntry(new KeyBindsList.KeyEntry(keymapping, component));
		}
	}
}
