package com.stereowalker.unionlib.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stereowalker.unionlib.client.gui.widget.list.ConfigList;
import com.stereowalker.unionlib.config.UnionConfig;

import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class ConfigScreen extends DefaultScreen {
	
	public ConfigList list;
	private Class<?> configClass;

	public ConfigScreen(Screen screen, Class<?> configClass, ITextComponent title) {
		super(title, screen);
		this.configClass = configClass;
	}

	@Override
	protected void init() {
		super.init();
		this.list = new ConfigList(minecraft, this, this.configClass.getAnnotation(UnionConfig.class));
		this.children.add(this.list);
		
		this.addButton(new Button(this.width / 2 - 100, this.height - 29, 200, 20, DialogTexts.GUI_DONE, (p_213124_1_) -> {
			this.minecraft.displayGuiScreen(this.previousScreen);
		}));
	}
	
	@Override
	public void drawOnScreen(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.list.render(matrixStack, mouseX, mouseY, partialTicks);
		super.drawOnScreen(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void tick() {
		this.list.tick();
		super.tick();
	}
	
	public void addChild(IGuiEventListener e) {
		this.children.add(e);
	}
}
