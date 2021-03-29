package com.stereowalker.unionlib.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stereowalker.unionlib.client.gui.widget.list.ModList;

import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

public class UnionModsScreen extends DefaultScreen {
	
	public ModList list;

	public UnionModsScreen(Screen screen) {
		super(new TranslationTextComponent("unionlib.mods.title"), screen);
	}

	@Override
	protected void init() {
		super.init();
		this.list = new ModList(minecraft, this);
		this.children.add(this.list);
		
		this.addButton(new Button(this.width / 2 - 100, this.height - 29, 200, 20, DialogTexts.GUI_DONE, (p_213124_1_) -> {
			this.minecraft.displayGuiScreen(this.previousScreen);
		}));
	}
	
	@Override
	public void drawOnScreen(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.list.render(matrixStack, mouseX, mouseY, partialTicks);
		super.drawOnScreen(matrixStack, mouseX, mouseY, partialTicks);
	}
}
