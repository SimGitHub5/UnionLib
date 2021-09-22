package com.stereowalker.unionlib.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.client.gui.widget.list.ModList;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;

public class UnionModsScreen extends DefaultScreen {
	
	public ModList list;

	public UnionModsScreen(Screen screen) {
		super(new TranslatableComponent("unionlib.mods.title"), screen);
	}

	@Override
	protected void init() {
		super.init();
		this.list = new ModList(minecraft, this);
		this.addWidget(this.list);
		
		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height - 29, 200, 20, CommonComponents.GUI_DONE, (p_213124_1_) -> {
			this.minecraft.setScreen(this.previousScreen);
		}));
	}
	
	@Override
	public void drawOnScreen(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.list.render(matrixStack, mouseX, mouseY, partialTicks);
		super.drawOnScreen(matrixStack, mouseX, mouseY, partialTicks);
	}
}
