package com.stereowalker.unionlib.client.gui.widget.button;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Slider extends AbstractSliderButton {

	@Nullable
	private Consumer<Double> responder;

	public Slider(int x, int y, int width, int height, double defaultValue) {
		super(x, y, width, height, new TextComponent(""), defaultValue);
		this.updateMessage();
	}

	public Slider(int x, int y, int width, int height, double defaultValue, Component message) {
		super(x, y, width, height, message, defaultValue);
		this.updateMessage();
	}

	@Override
	protected void updateMessage() {

	}

	@Override
	protected void applyValue() {
		onValueChange(this.value);

	}

	public double getSliderValue(){
		return value;
	}

	public void setResponder(Consumer<Double> pResponder) {
		this.responder = pResponder;
	}

	private void onValueChange(double pNewText) {
		if (this.responder != null) {
			this.responder.accept(pNewText);
		}

	}
}