package com.stereowalker.unionlib.util;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenHelper {
	public enum ScreenOffset {
		TOP_LEFT, TOP, TOP_RIGHT,

		LEFT, CENTER, RIGHT,

		BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT;

	}
	
	static Window window = Minecraft.getInstance().getWindow();
	
	public static int getYOffset(ScreenOffset pos) {
		if (pos.equals(ScreenOffset.TOP_LEFT) || pos.equals(ScreenOffset.TOP) || pos.equals(ScreenOffset.TOP_RIGHT)) {
			return 0;
		}
		
		if (pos.equals(ScreenOffset.LEFT) || pos.equals(ScreenOffset.CENTER) || pos.equals(ScreenOffset.RIGHT)) {
			return window.getGuiScaledHeight() / 2;
		}
		
		if (pos.equals(ScreenOffset.BOTTOM_LEFT) || pos.equals(ScreenOffset.BOTTOM) || pos.equals(ScreenOffset.BOTTOM_RIGHT)) {
			return window.getGuiScaledHeight();
		}
		return 0;
	}
	
	public static int getXOffset(ScreenOffset pos) {
		if (pos.equals(ScreenOffset.TOP_LEFT) || pos.equals(ScreenOffset.LEFT) || pos.equals(ScreenOffset.BOTTOM_LEFT)) {
			return 0;
		}
		
		if (pos.equals(ScreenOffset.TOP) || pos.equals(ScreenOffset.CENTER) || pos.equals(ScreenOffset.BOTTOM)) {
			return window.getGuiScaledHeight() / 2;
		}
		
		if (pos.equals(ScreenOffset.TOP_RIGHT) || pos.equals(ScreenOffset.RIGHT) || pos.equals(ScreenOffset.BOTTOM_RIGHT)) {
			return window.getGuiScaledHeight();
		}
		return 0;
	}
}
