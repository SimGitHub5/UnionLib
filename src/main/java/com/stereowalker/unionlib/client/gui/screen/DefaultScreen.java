package com.stereowalker.unionlib.client.gui.screen;


import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class DefaultScreen extends Screen {
	public final Screen previousScreen;

	public DefaultScreen(Component titleIn, Screen screen) {
		super(titleIn);
		this.previousScreen = screen;
	}
	
	public abstract void drawOnScreen(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks);

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		this.drawOnScreen(matrixStack, mouseX, mouseY, partialTicks);
		GuiComponent.drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 15, 16777215);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.previousScreen);
	}
}
