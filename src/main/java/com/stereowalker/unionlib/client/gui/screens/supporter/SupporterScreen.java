package com.stereowalker.unionlib.client.gui.screens.supporter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.client.gui.screen.DefaultScreen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;

public class SupporterScreen extends DefaultScreen {

	public SupporterList list;

	public SupporterScreen(Screen screen) {
		super(new TranslatableComponent("My Valuable Supporters On Patreon at"), screen);
	}

	@Override
	protected void init() {
		super.init();
		this.list = new SupporterList(minecraft, this);
		this.addWidget(this.list);

		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height - 29, 200, 20, CommonComponents.GUI_DONE, (p_213124_1_) -> {
			this.minecraft.setScreen(this.previousScreen);
		}));
	}

	@Override
	public void drawOnScreen(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.list.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	public <T extends GuiEventListener & NarratableEntry> T addChild(T e) {
		return this.addWidget(e);
	}
}
