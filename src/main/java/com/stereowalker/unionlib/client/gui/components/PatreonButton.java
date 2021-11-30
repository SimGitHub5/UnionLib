package com.stereowalker.unionlib.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.UnionLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class PatreonButton extends Button {
	public PatreonButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
		super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
	}

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
}
