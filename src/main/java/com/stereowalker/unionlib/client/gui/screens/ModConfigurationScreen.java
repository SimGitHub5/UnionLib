package com.stereowalker.unionlib.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.client.gui.screen.DefaultScreen;
import com.stereowalker.unionlib.client.gui.screens.controls.ModControlsScreen;
import com.stereowalker.unionlib.mod.MinecraftMod;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;

public class ModConfigurationScreen extends DefaultScreen {
	private Button configButton;
	private Button keyButton;
	private MinecraftMod mod;

	public ModConfigurationScreen(MinecraftMod mod, Screen screen) {
		super(new TranslatableComponent("unionlib.setup.title"), screen);
		this.mod = mod;
	}

	@Override
	protected void init() {
		this.configButton = addRenderableWidget(new Button(this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, new TranslatableComponent("union.gui.config"), (onPress) -> {
			if (this.mod.getConfigScreen(minecraft, this) != null) {
				minecraft.setScreen(mod.getConfigScreen(minecraft, this));
			}
		}));
		this.configButton.active = this.mod.getConfigScreen(minecraft, this) != null;

		this.keyButton = this.addRenderableWidget(new Button(this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20, new TranslatableComponent("union.gui.controls"), (onPress) -> {
			if (this.mod.getModKeyMappings().length > 0) {
				minecraft.setScreen(new ModControlsScreen(mod, this, this.minecraft.options));
			}
		}));
		this.keyButton.active = this.mod.getModKeyMappings().length > 0;

		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, CommonComponents.GUI_DONE, (p_96257_) -> {
			this.minecraft.setScreen(this.previousScreen);
		}));
	}

	@Override
	public void drawOnScreen(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {

	}

}
