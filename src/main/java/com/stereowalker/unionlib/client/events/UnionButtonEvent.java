package com.stereowalker.unionlib.client.events;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.client.gui.screen.UnionModsScreen;
import com.stereowalker.unionlib.client.gui.widget.button.OverlayImageButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class UnionButtonEvent {
	private static final ResourceLocation CONTROLLER_BUTTON_TEXTURES = UnionLib.location("textures/gui/union_button.png");

	public static Screen getCurrentScreen() {
		return Minecraft.getInstance().currentScreen;
	}

	public static MouseHelper getMouse() {
		return Minecraft.getInstance().mouseHelper;
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void drawButtons(GuiScreenEvent.InitGuiEvent event) {
		if (UnionLib.drawMainMenuButton()) {
		if(event.getGui() instanceof MainMenuScreen) {
			event.addWidget(new OverlayImageButton(event.getGui().width / 2 + 104, event.getGui().height / 4 + 48 + 24 * 2, 20, 20, 0, 0, 20, CONTROLLER_BUTTON_TEXTURES, 20, 40, (p_213088_1_) -> {
				event.getGui().getMinecraft().displayGuiScreen(new UnionModsScreen(event.getGui()));
			}, I18n.format("menu.button.union")));
		}
		if(event.getGui() instanceof IngameMenuScreen) {
			event.addWidget(new OverlayImageButton(event.getGui().width / 2 + 104, event.getGui().height / 4 + 120 + -16, 20, 20, 0, 0, 20, CONTROLLER_BUTTON_TEXTURES, 20, 40, (p_213088_1_) -> {
				event.getGui().getMinecraft().displayGuiScreen(new UnionModsScreen(event.getGui()));
			}, I18n.format("menu.button.union")));
		}
		}
	}
}
