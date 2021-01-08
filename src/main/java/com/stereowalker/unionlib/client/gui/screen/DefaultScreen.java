package com.stereowalker.unionlib.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class DefaultScreen extends Screen{
	private final Screen previousScreen;

	public DefaultScreen(ITextComponent titleIn, Screen screen) {
		super(titleIn);
		this.previousScreen = screen;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		AbstractGui.drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 15, 16777215);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void closeScreen() {
		this.minecraft.displayGuiScreen(this.previousScreen);
	}
}
