package com.stereowalker.unionlib.client.gui.screens.config.lists;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.client.gui.screen.DefaultScreen;
import com.stereowalker.unionlib.config.ConfigBuilder;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ListScreen extends DefaultScreen {

	public ListList<?> list;
	private ConfigValue<List<?>> configList;

	public ListScreen(Component name, Screen screen, ConfigValue<List<?>> config) {
		super(name, screen);
		this.configList = config;
	}

	@Override
	protected void init() {
		super.init();
		this.list = new ListList(minecraft, this, this.configList);
		this.addWidget(this.list);

		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height - 29, 200, 20, CommonComponents.GUI_DONE, (p_213124_1_) -> {
			this.configList.set(this.list.mainList);
			ConfigBuilder.reload();
			this.minecraft.setScreen(this.previousScreen);
		}));
	}

	@Override
	public void drawOnScreen(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.list.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void onClose() {
		this.configList.set(this.list.mainList);
		ConfigBuilder.reload();
		super.onClose();
	}

	@Override
	public void tick() {
		this.list.tick();
		super.tick();
	}

	public <T extends GuiEventListener & NarratableEntry> T addChild(T e) {
		return this.addWidget(e);
	}
}
