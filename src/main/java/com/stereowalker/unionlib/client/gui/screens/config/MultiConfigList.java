package com.stereowalker.unionlib.client.gui.screens.config;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.config.ConfigClassBuilder;
import com.stereowalker.unionlib.config.ConfigObject;
import com.stereowalker.unionlib.config.ConfigObjectBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

@Environment(EnvType.CLIENT)
public class MultiConfigList extends ContainerObjectSelectionList<MultiConfigList.Entry> {

	public MultiConfigList(Minecraft mcIn, MinecraftModConfigsScreen screen) {
		super(mcIn, screen.width +45, screen.height, 43, screen.height - 32, 25);
		if (screen.configObjects != null)
			for(ConfigObject obj : screen.configObjects) {
				this.addEntry(new MultiConfigList.ModConfigObjectEntry(obj, screen));
			}
		if (screen.configClasses != null)
			for(Class<?> obj : screen.configClasses) {
				this.addEntry(new MultiConfigList.ModConfigClassEntry(obj, screen));
			}

	}

	protected int getScrollbarPosition() {
		return super.getScrollbarPosition() + 15 + 40;
	}

	public int getRowWidth() {
		return super.getRowWidth() + 29;
	}

	@Environment(EnvType.CLIENT)
	public abstract static class Entry extends ContainerObjectSelectionList.Entry<MultiConfigList.Entry> {
		/** The localized key description for this KeyEntry */
		protected Button configButton;
		
		public void render(PoseStack pMatrixStack, int p_230432_2_, int p_230432_3_, int p_230432_4_, int pWidth, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			this.configButton.x = p_230432_4_;
			this.configButton.y = p_230432_3_;
			this.configButton.render(pMatrixStack, p_230432_7_, p_230432_8_, p_230432_10_);
		}

		@Override
		public List<? extends GuiEventListener> children() {
			return ImmutableList.of(this.configButton);
		}

		@Override
		public List<? extends NarratableEntry> narratables() {
			return ImmutableList.of(this.configButton);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			return this.configButton.mouseClicked(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button) {
			return this.configButton.mouseReleased(mouseX, mouseY, button);
		}
	}

	@Environment(EnvType.CLIENT)
	public class ModConfigObjectEntry extends MultiConfigList.Entry {
		private ModConfigObjectEntry(final ConfigObject obj, final Screen screen) {
			this.configButton = new Button(0, 0, 200, 20, ConfigObjectBuilder.getConfigName(obj), (onPress) -> {
				minecraft.setScreen(new ConfigScreen(screen, obj));
			});
		}
	}

	@Environment(EnvType.CLIENT)
	public class ModConfigClassEntry extends MultiConfigList.Entry {
		private ModConfigClassEntry(final Class<?> cla, final Screen screen) {
			this.configButton = new Button(0, 0, 200, 20, ConfigClassBuilder.getConfigName(cla), (onPress) -> {
				minecraft.setScreen(new ConfigScreen(screen, cla));
			});
		}
	}
}
