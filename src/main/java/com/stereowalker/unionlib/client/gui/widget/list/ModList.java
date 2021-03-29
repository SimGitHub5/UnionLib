package com.stereowalker.unionlib.client.gui.widget.list;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.client.gui.screen.UnionModsScreen;
import com.stereowalker.unionlib.client.gui.widget.button.OverlayImageButton;
import com.stereowalker.unionlib.mod.UnionMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModList extends AbstractOptionList<ModList.Entry> {

	public ModList(Minecraft mcIn, UnionModsScreen screen) {
		super(mcIn, screen.width +45, screen.height, 43, screen.height - 32, 25);
		for(UnionMod mod : UnionLib.mods) {
			this.addEntry(new ModList.ModEntry(mod, screen));
		}

	}

	protected int getScrollbarPosition() {
		return super.getScrollbarPosition() + 15 + 40;
	}

	public int getRowWidth() {
		return super.getRowWidth() + 72;
	}

	@OnlyIn(Dist.CLIENT)
	public abstract static class Entry extends AbstractOptionList.Entry<ModList.Entry> {
	}

	@OnlyIn(Dist.CLIENT)
	public class ModEntry extends ModList.Entry {
		/** The mod */
		private final UnionMod mod;
		private final Screen screen;
		/** The localized key description for this KeyEntry */
		private final Button modImage;
		private final Button configButton;

		private ModEntry(final UnionMod mod, final Screen screen) {
			this.mod = mod;
			this.screen = screen;
			
			this.modImage = new OverlayImageButton(0, 0, 20, 20, 0, 0, 20, mod.getModTexture(), 20, 40, (onPress) -> {
					}, new TranslationTextComponent("menu.button.union"));
			
			this.configButton = new Button(0, 0, 200, 20, new TranslationTextComponent("union.gui.config"), (onPress) -> {
				if (this.mod.getConfigScreen(minecraft, this.screen) != null) {
					minecraft.displayGuiScreen(mod.getConfigScreen(minecraft, this.screen));
				}
			});
		}

		public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			this.modImage.x = p_230432_4_;
			this.modImage.y = p_230432_3_;
			this.modImage.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
			this.configButton.x = p_230432_4_ + 50;
			this.configButton.y = p_230432_3_;
			this.configButton.active = mod.getConfigScreen(minecraft, this.screen) != null;
			this.configButton.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
		}

		public List<? extends IGuiEventListener> getEventListeners() {
			return ImmutableList.of(this.modImage, this.configButton);
		}

		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (this.modImage.mouseClicked(mouseX, mouseY, button)) {
				return true;
			} else {
				return this.configButton.mouseClicked(mouseX, mouseY, button);
			}
		}

		public boolean mouseReleased(double mouseX, double mouseY, int button) {
			return this.modImage.mouseReleased(mouseX, mouseY, button) || this.configButton.mouseReleased(mouseX, mouseY, button);
		}
	}
}
