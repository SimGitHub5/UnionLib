package com.stereowalker.unionlib.client.gui.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.client.keybindings.KeyBindings;
import com.stereowalker.unionlib.inventory.container.UnionContainer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

@Environment(EnvType.CLIENT)
public class UnionInventoryScreen extends EffectRenderingInventoryScreen<UnionContainer> implements RecipeUpdateListener {
	private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
	/** The old x position of the mouse pointer */
	private float oldMouseX;
	/** The old y position of the mouse pointer */
	private float oldMouseY;
	private final RecipeBookComponent recipeBookGui = new RecipeBookComponent();
	private boolean removeRecipeBookGui;
	private boolean widthTooNarrow;
	private boolean buttonClicked;

	public UnionInventoryScreen(UnionContainer container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		this.passEvents = true;
		this.titleLabelX = 97;
	}

	@Override
	public void containerTick() {
		this.recipeBookGui.tick();
	}

	@Override
	protected void init() {
		super.init();
		this.widthTooNarrow = this.width < 379;
		this.recipeBookGui.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
		this.removeRecipeBookGui = true;
		this.leftPos = this.recipeBookGui.updateScreenPosition(this.width, this.imageWidth);
		this.addWidget(this.recipeBookGui);
		this.setInitialFocus(this.recipeBookGui);
		this.addRenderableWidget(new ImageButton(this.leftPos + 104, this.height / 2 - 22, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (p_214086_1_) -> {
			this.recipeBookGui.toggleVisibility();
			this.leftPos = this.recipeBookGui.updateScreenPosition(this.width, this.imageWidth);
			((ImageButton)p_214086_1_).setPosition(this.leftPos + 104, this.height / 2 - 22);
			this.buttonClicked = true;
		}));
	}

	@Override
	protected void renderLabels(PoseStack matrixStack, int x, int y) {
		this.font.draw(matrixStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		//		this.doRenderEffects = !this.recipeBookGui.isVisible(); TODO: Figure out what that was
		if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
			this.renderBg(matrixStack, partialTicks, mouseX, mouseY);
			this.recipeBookGui.render(matrixStack, mouseX, mouseY, partialTicks);
		} else {
			this.recipeBookGui.render(matrixStack, mouseX, mouseY, partialTicks);
			super.render(matrixStack, mouseX, mouseY, partialTicks);
			this.recipeBookGui.renderGhostRecipe(matrixStack, this.leftPos, this.topPos, false, partialTicks);
		}

		this.renderTooltip(matrixStack, mouseX, mouseY);
		this.recipeBookGui.renderTooltip(matrixStack, this.leftPos, this.topPos, mouseX, mouseY);
		this.oldMouseX = (float)mouseX;
		this.oldMouseY = (float)mouseY;
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, UnionLib.Locations.UNION_INVENTORY_BACKGROUND);
		int i = this.leftPos;
		int j = this.topPos;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		InventoryScreen.renderEntityInInventory(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.minecraft.player);
	}

	@Override
	protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
		return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.isHovering(x, y, width, height, mouseX, mouseY);
	}

	public boolean mouseClicked(double mouseX, double mouseY, int p_231044_5_) {
		if (this.recipeBookGui.mouseClicked(mouseX, mouseY, p_231044_5_)) {
			this.setFocused(this.recipeBookGui);
			return true;
		} else {
			return this.widthTooNarrow && this.recipeBookGui.isVisible() ? false : super.mouseClicked(mouseX, mouseY, p_231044_5_);
		}
	}

	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (this.buttonClicked) {
			this.buttonClicked = false;
			return true;
		} else {
			return super.mouseReleased(mouseX, mouseY, button);
		}
	}

	protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
		boolean flag = mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.imageWidth) || mouseY >= (double)(guiTopIn + this.imageHeight);
		return this.recipeBookGui.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseButton) && flag;
	}

	/**
	 * Called when the mouse is clicked over a slot or outside the gui.
	 */
	@Override
	protected void slotClicked(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.slotClicked(slotIn, slotId, mouseButton, type);
		this.recipeBookGui.slotClicked(slotIn);
	}

	public void recipesUpdated() {
		this.recipeBookGui.recipesUpdated();
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
			return true;
		} else if (KeyBindings.OPEN_UNION_INVENTORY.matches(pKeyCode, pScanCode)) {
			this.onClose();
			return true;
		} else {return false;}
	}

	@Override
	public void onClose() {
		if (this.removeRecipeBookGui) {
			this.recipeBookGui.removed();
		}

		super.onClose();
	}

	@Override
	public RecipeBookComponent getRecipeBookComponent() {
		return this.recipeBookGui;
	}
}