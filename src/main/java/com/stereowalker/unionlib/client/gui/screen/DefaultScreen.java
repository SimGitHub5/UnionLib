package com.stereowalker.unionlib.client.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class DefaultScreen extends Screen{
	private final Screen previousScreen;

	public DefaultScreen(ITextComponent titleIn, Screen screen) {
		super(titleIn);
		this.previousScreen = screen;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		this.drawCenteredString(this.font, this.title.toString(), this.width / 2, 15, 16777215);
		super.render(mouseX, mouseY, partialTicks);
	}

	@Override
	public void onClose() {
		this.minecraft.displayGuiScreen(this.previousScreen);
	}
}
