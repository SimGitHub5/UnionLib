package com.stereowalker.unionlib.client.events;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.client.gui.screens.UnionModsScreen;
import com.stereowalker.unionlib.client.gui.widget.button.OverlayImageButton;

import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class UnionButtonEvent {
	private static final ResourceLocation CONTROLLER_BUTTON_TEXTURES = UnionLib.location("textures/gui/union_button.png");

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void drawButtons(ScreenEvent.InitScreenEvent event) {
		if (UnionLib.CONFIG.config_button) {
			if(event.getScreen() instanceof TitleScreen) {
				event.addListener(new OverlayImageButton(event.getScreen().width / 2 + 104, event.getScreen().height / 4 + 48 + 24 * 2, 20, 20, 0, 0, 20, CONTROLLER_BUTTON_TEXTURES, 20, 40, (p_213088_1_) -> {
					event.getScreen().getMinecraft().setScreen(new UnionModsScreen(event.getScreen()));
				}, new TranslatableComponent("menu.button.union")));
			}
			if(event.getScreen() instanceof PauseScreen) {
				event.addListener(new OverlayImageButton(event.getScreen().width / 2 + 104, event.getScreen().height / 4 + 120 + -16, 20, 20, 0, 0, 20, CONTROLLER_BUTTON_TEXTURES, 20, 40, (p_213088_1_) -> {
					event.getScreen().getMinecraft().setScreen(new UnionModsScreen(event.getScreen()));
				}, new TranslatableComponent("menu.button.union")));
			}
		}
	}
}
