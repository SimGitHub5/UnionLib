package com.stereowalker.unionlib.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.UnionLib;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OverlayImageButton extends ImageButton {
	private final ResourceLocation overlayLocation;
	private final int xTexStart;
	private final int yTexStart;
	private final int textureWidth;
	private final int textureHeight;

	public OverlayImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, ResourceLocation overlayLocation, int textureWidth, int textureHeight, Button.OnPress onPress, Component title) {
		this(x, y, width, height, xTexStart, yTexStart, 20, UnionLib.Locations.OVERLAY_BUTTON_IMAGE_BACKGROUND, overlayLocation, textureWidth, textureHeight, onPress, title);
	}

	public OverlayImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation overlayLocation, int textureWidth, int textureHeight, Button.OnPress onPress, Component title) {
		this(x, y, width, height, xTexStart, yTexStart, yDiffText, UnionLib.Locations.OVERLAY_BUTTON_IMAGE_BACKGROUND, overlayLocation, textureWidth, textureHeight, onPress, title);
	}

	public OverlayImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation resourceLocation, ResourceLocation overlayLocation, int textureWidth, int textureHeight, Button.OnPress onPress, Component title) {
		super(x, y, width, height, xTexStart, yTexStart, yDiffText, resourceLocation, textureWidth, textureHeight, onPress, title);
		this.overlayLocation = overlayLocation;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.xTexStart = xTexStart;
		this.yTexStart = yTexStart;
	}

	@Override
	public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, this.overlayLocation);
		int i = this.yTexStart;
		RenderSystem.enableDepthTest();
		blit(matrixStack, this.x, this.y, (float)this.xTexStart, (float)i, this.width, this.height, this.textureWidth, this.textureHeight/2);
	}
}
