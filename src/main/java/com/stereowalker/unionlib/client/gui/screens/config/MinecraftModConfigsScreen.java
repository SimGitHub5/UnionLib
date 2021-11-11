package com.stereowalker.unionlib.client.gui.screens.config;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.client.gui.screen.DefaultScreen;
import com.stereowalker.unionlib.config.ConfigObject;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class MinecraftModConfigsScreen extends DefaultScreen {
	
	public MultiConfigList list;
	public ConfigObject[] configObjects;
	public Class<?>[] configClasses;

	public MinecraftModConfigsScreen(Screen screen, Component title, ConfigObject[] configObjects, Class<?>[] configClasses) {
		super(title, screen);
		this.configClasses = configClasses;
		this.configObjects = configObjects;
	}
	
	public MinecraftModConfigsScreen(Screen screen, Component title, Class<?>... configClasses) {
		this(screen, title, null, configClasses);
	}
	
	public MinecraftModConfigsScreen(Screen screen, Component title, ConfigObject... configObjects) {
		this(screen, title, configObjects, null);
	}

	@Override
	protected void init() {
		super.init();
		this.list = new MultiConfigList(minecraft, this);
		this.addWidget(this.list);
		
		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height - 29, 200, 20, CommonComponents.GUI_DONE, (p_213124_1_) -> {
			this.minecraft.setScreen(this.previousScreen);
		}));
	}
	
	@Override
	public void drawOnScreen(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.list.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}
