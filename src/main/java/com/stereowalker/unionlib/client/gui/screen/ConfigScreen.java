package com.stereowalker.unionlib.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.client.gui.widget.list.ConfigList;
import com.stereowalker.unionlib.config.ConfigObject;
import com.stereowalker.unionlib.config.UnionConfig;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends DefaultScreen {

	public ConfigList list;
	private Class<?> configClass;
	private ConfigObject configObject;

	public ConfigScreen(Screen screen, Class<?> configClass, Component title) {
		super(title, screen);
		this.configClass = configClass;
		this.configObject = null;
	}

	public ConfigScreen(Screen screen, ConfigObject configObject, Component title) {
		super(title, screen);
		this.configObject = configObject;
		this.configClass = null;
	}

	@Override
	protected void init() {
		super.init();
		if (this.configClass != null)
			this.list = new ConfigList(minecraft, this, this.configClass.getAnnotation(UnionConfig.class));
		else if (this.configObject != null)
			this.list = new ConfigList(minecraft, this, this.configObject.getClass().getAnnotation(UnionConfig.class));
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

	@Override
	public void tick() {
		this.list.tick();
		super.tick();
	}

	public <T extends GuiEventListener & NarratableEntry> T addChild(T e) {
		return this.addWidget(e);
	}
}
