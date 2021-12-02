package com.stereowalker.unionlib.client.gui.screens.controls;

import com.stereowalker.unionlib.mod.MinecraftMod;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;

public class ModControlsScreen extends KeyBindsScreen {
	public MinecraftMod mod;
	
	public ModControlsScreen(MinecraftMod mod, Screen p_97519_, Options p_97520_) {
		super(p_97519_, p_97520_);
		this.mod = mod;
	}

	@Override
	protected void init() {
		this./*controlList*/f_193977_ = new ModKeyBindsList(this, this.minecraft);
		this.addWidget(this./*controlList*/f_193977_);
		this./*resetButton*/f_193978_ = this.addRenderableWidget(new Button(this.width / 2 - 155, this.height - 29, 150, 20, new TranslatableComponent("controls.resetAll"), (p_97538_) -> {
			for(KeyMapping keymapping : this.mod.getModKeyMappings()) {
				keymapping.setToDefault();
			}

			KeyMapping.resetMapping();
		}));
		this.addRenderableWidget(new Button(this.width / 2 - 155 + 160, this.height - 29, 150, 20, CommonComponents.GUI_DONE, (p_97535_) -> {
			this.minecraft.setScreen(this.lastScreen);
		}));
	}
}
