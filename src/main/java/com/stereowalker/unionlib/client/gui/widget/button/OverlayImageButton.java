package com.stereowalker.unionlib.client.gui.widget.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stereowalker.unionlib.UnionLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OverlayImageButton extends ImageButton {
	private final ResourceLocation overlayLocation;
	private final int xTexStart;
	private final int yTexStart;
	private final int textureWidth;
	private final int textureHeight;

	public OverlayImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, ResourceLocation overlayLocation, int textureWidth, int textureHeight, Button.IPressable onPress, String title) {
		this(x, y, width, height, xTexStart, yTexStart, 20, UnionLib.location("textures/gui/button_background.png"), overlayLocation, textureWidth, textureHeight, onPress, title);
	}
	
	public OverlayImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation overlayLocation, int textureWidth, int textureHeight, Button.IPressable onPress, String title) {
		this(x, y, width, height, xTexStart, yTexStart, yDiffText, UnionLib.location("textures/gui/button_background.png"), overlayLocation, textureWidth, textureHeight, onPress, title);
	}
	
	public OverlayImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation resourceLocation, ResourceLocation overlayLocation, int textureWidth, int textureHeight, Button.IPressable onPress, String title) {
		super(x, y, width, height, xTexStart, yTexStart, yDiffText, resourceLocation, textureWidth, textureHeight, onPress, title);
		this.overlayLocation = overlayLocation;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.xTexStart = xTexStart;
		this.yTexStart = yTexStart;
	}

	@Override
	public void renderButton(int mouseX, int mouseY, float partialTicks) {
		super.renderButton(mouseX, mouseY, partialTicks);
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(this.overlayLocation);
		int i = this.yTexStart;
		RenderSystem.enableDepthTest();
		blit(this.x, this.y, (float)this.xTexStart, (float)i, this.width, this.height, this.textureWidth, this.textureHeight/2);
	}
}
