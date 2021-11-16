package com.stereowalker.unionlib.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.client.gui.components.ModList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;

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

		this.addRenderableWidget(new Button(this.width / 2 - 155, this.height - 29, 150, 20, CommonComponents.GUI_DONE, (p_213124_1_) -> {
			this.minecraft.setScreen(this.previousScreen);
		}));

		this.addRenderableWidget(new Button(this.width / 2 - 155 + 160, this.height - 29, 150, 20, CommonComponents.GUI_DONE, (p_213124_1_) -> {
			this.minecraft.setScreen(this.previousScreen);
		}) {
			@Override
			public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
				Minecraft minecraft = Minecraft.getInstance();
				Font font = minecraft.font;
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderTexture(0, UnionLib.location("textures/gui/patreon_button.png"));
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
				int i = this.getYImage(this.isHovered());
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.enableDepthTest();
				this.blit(pPoseStack, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
				this.blit(pPoseStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
				this.renderBg(pPoseStack, minecraft, pMouseX, pMouseY);
				int j = getFGColor();
				drawCenteredString(pPoseStack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
			}

			@Override
			public int getFGColor() {
				if (packedFGColor != UNSET_FG_COLOR) return packedFGColor;
				return this.active ? 470090 : 10526880; // Blue : Light Grey
			}
		});
	}

	@Override
	public void drawOnScreen(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.list.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}
