package com.stereowalker.unionlib.client.gui.screen;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.client.gui.widget.button.OverlayImageButton;
import com.stereowalker.unionlib.mod.UnionMod;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

public class UnionModsScreen extends DefaultScreen {

	public UnionModsScreen(Screen screen) {
		super(new TranslationTextComponent("unionlib.mods.title"), screen);
	}

	@Override
	protected void init() {
		int imgOffset = -110;
		int configOffset = -85;
		int i = 30;
		for (int j = 0; j < 4; j++) {
			if (UnionLib.mods.size() > j) {
				UnionMod mod = UnionLib.mods.get(j);
				this.addButton(
						new OverlayImageButton(this.width / 2 + imgOffset, this.height / 6 + i*j, 20, 20, 0, 0, 20, mod.getModTexture(), 20, 40, (onPress) -> {
						}, new TranslationTextComponent("menu.button.union")));

				Button config = this.addButton(new Button(this.width / 2 + configOffset, this.height / 6 + i*j, 200, 20, new TranslationTextComponent("gui.config"), (onPress) -> {
					if (mod.getConfigScreen(minecraft, this) != null) {
						this.getMinecraft().displayGuiScreen(mod.getConfigScreen(minecraft, this));
					}
				}));
				config.active = mod.getConfigScreen(minecraft, this) != null;
			}
		}
		super.init();
	}
}
