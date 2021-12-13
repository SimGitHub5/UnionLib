package com.stereowalker.unionlib.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.UnionLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class OverflowTextButton extends Button {
	public OverflowTextButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
		super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
		remainingText = pMessage;
		passedText = new TextComponent("");
	}

	public OverflowTextButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Button.OnPress pOnPress, Button.OnTooltip pOnTooltip) {
		super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pOnTooltip);
		remainingText = pMessage;
		passedText = new TextComponent("");
	}

	@Override
	public void setMessage(Component pMessage) {
		remainingText = pMessage;
		super.setMessage(pMessage);
	}

	/**
	 * Draws this button to the screen.
	 */
	Component passedText;
	Component remainingText;
	int ticks;
	int completedTicks;
	@Override
	public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float partial)
	{
		Minecraft minecraft = Minecraft.getInstance();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
		int i = this.getYImage(this.isHoveredOrFocused());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		this.blit(pPoseStack, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
		this.blit(pPoseStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
		this.renderBg(pPoseStack, minecraft, pMouseX, pMouseY);

		boolean hasOverflow = false;
		boolean shouldhaveOverflow = false;

		Component buttonText = remainingText;
		int strWidth = minecraft.font.width(buttonText);
		int ellipsisWidth = minecraft.font.width("...");

		int actualStrWidth = minecraft.font.width(this.getMessage());
		int actualEllipsisWidth = minecraft.font.width("...");

		if (strWidth > width - 6 && strWidth > ellipsisWidth) {
			hasOverflow = true;
			//TODO, srg names make it hard to figure out how to append to an ITextProperties from this trim operation, wraping this in StringTextComponent is kinda dirty.
			buttonText = new TextComponent(minecraft.font.substrByWidth(buttonText, width - 6 - ellipsisWidth).getString() + "...");
		} else {
			hasOverflow = false;
		}

		if (actualStrWidth > width - 6 && actualStrWidth > actualEllipsisWidth) {
			shouldhaveOverflow = true;
		} else {
			shouldhaveOverflow = false;
		}

		if (this.isHoveredOrFocused() && hasOverflow) {
			ticks++;
			if (ticks >= UnionLib.CONFIG.textScrollSpeed) {
				ticks = 0;

				passedText = new TextComponent(passedText.getString()+remainingText.getString().charAt(0));
				remainingText = new TextComponent(remainingText.getString().substring(1));
			}
		} else if (this.isHoveredOrFocused() && shouldhaveOverflow && completedTicks < UnionLib.CONFIG.textScrollReset) {
			completedTicks++;
		} else {
			ticks = 0;
			completedTicks = 0;
			remainingText = this.getMessage();
			passedText = new TextComponent("");
		}

		drawCenteredString(pPoseStack, minecraft.font, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, this.active ? 0xFFFFFF : 0xA0A0A0);
	
		if (this.isHoveredOrFocused()) {
			this.renderToolTip(pPoseStack, pMouseX, pMouseY);
		}
	}
}
