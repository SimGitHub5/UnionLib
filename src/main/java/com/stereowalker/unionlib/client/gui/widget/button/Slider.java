package com.stereowalker.unionlib.client.gui.widget.button;

import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Slider extends AbstractSlider {

	public Slider(int x, int y, int width, int height, double defaultValue) {
		super(x, y, width, height, new StringTextComponent(""), defaultValue);
		this./*updateMessage*/func_230979_b_();
	}
	
	public Slider(int x, int y, int width, int height, double defaultValue, ITextComponent message) {
		super(x, y, width, height, message, defaultValue);
		this./*updateMessage*/func_230979_b_();
	}

	@Override
	protected void /*updateMessage*/func_230979_b_() {

	}

	@Override
	protected void /*applyValue*/func_230972_a_() {

	}
	
	public double getSliderValue(){
		return sliderValue;
	}
}