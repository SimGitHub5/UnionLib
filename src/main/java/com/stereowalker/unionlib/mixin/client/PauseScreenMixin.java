package com.stereowalker.unionlib.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.client.gui.screens.UnionModsScreen;
import com.stereowalker.unionlib.client.gui.widget.button.OverlayImageButton;

import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {

	protected PauseScreenMixin(Component pTitle) {
		super(pTitle);
	}

	@Inject(method = "init", at = @At("TAIL"))
	public void init_inject(CallbackInfo ci) {
		if (UnionLib.CONFIG.config_button) {
			this.addRenderableWidget(new OverlayImageButton(this.width / 2 + 104, this.height/ 4 + 120 + -16, 20, 20, 0, 0, 20, UnionLib.Locations.UNION_BUTTON_IMAGE, 20, 40, (p_213088_1_) -> {
				this.getMinecraft().setScreen(new UnionModsScreen(this));
			}, new TranslatableComponent("menu.button.union")));
		}
	}
}
